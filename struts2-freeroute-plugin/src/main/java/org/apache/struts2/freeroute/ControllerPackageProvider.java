package org.apache.struts2.freeroute;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.PackageProvider;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.freeroute.annotation.MethodType;
import org.apache.struts2.freeroute.annotation.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 对 controller 进行扫描，并对其进行配置(PackageConfig, ActionConfig)
 *
 * @author bastengao
 * @date 12-12-16 13:56
 */
public class ControllerPackageProvider implements PackageProvider {
    private static final Logger log = LoggerFactory.getLogger(ControllerPackageProvider.class);

    private Configuration configuration;
    private RouteMappingHandler routeMappingHandler;
    // controller 所在的包
    private String controllerPackage;
    // 默认父包 (如果没有配置，默认为 "struts-default")
    private String defaultParentPackage;

    @Inject("routeMappingHandler")
    private void setRouteMappingHandler(RouteMappingHandler routeMappingHandler) {
        log.trace("routeMappingHandler:{}", routeMappingHandler);
        this.routeMappingHandler = routeMappingHandler;
    }

    @Inject(value = "struts.freeroute.controllerPackage", required = true)
    private void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    @Inject(value = "struts.freeroute.defaultParentPackage", required = true)
    private void setDefaultParentPackage(String defaultParentPackage) {
        this.defaultParentPackage = defaultParentPackage;
    }

    @Override
    public void init(Configuration configuration) throws ConfigurationException {
        log.debug("init");
        this.configuration = configuration;
    }

    @Override
    public boolean needsReload() {
        log.debug("needsReload:false");
        return false;
    }

    @Override
    public void loadPackages() throws ConfigurationException {
        log.debug("loadPackages");

        Map<String, PackageConfig.Builder> packages = createPackageConfig();
        for (Map.Entry<String, PackageConfig.Builder> entry : packages.entrySet()) {
            configuration.addPackageConfig(entry.getValue().getName(), entry.getValue().build());
        }
    }

    private Map<String, PackageConfig.Builder> createPackageConfig() {
        Map<String, PackageConfig.Builder> packages = new HashMap<String, PackageConfig.Builder>();

        try {
            //分析所有的 "Controller"
            for (ClassPath.ClassInfo classInfo : findControllers(controllerPackage)) {
                List<RouteMapping> routeMappings = parseController(classInfo.load());
                for (RouteMapping routeMapping : routeMappings) {
                    // 同样的路径, 不同的 method 映射的是不同的 action

                    String routePath = routeMapping.getRoute().value();
                    routePath = ActionUtil.padSlash(routePath);
                    //routePath = RouteUtil.flatRoutePath(routePath);

                    /**
                     * 如果 routePath 中有 pathVariable,
                     * 例如 "/persons/{id}" 那么将路由转化为 "/persons/__id__"
                     * "/persons/{id}/edit" 转化为 "/persons/__id__/edit"
                     *
                     * TODO 将动态路由映射的 action 放到黑明单里，
                     * 如果直接访问 "/persons/__id__/edit" 则直接拦截
                     */

                    //添加路由映射
                    routeMappingHandler.put(routePath, routeMapping);

                    ActionInfo actionInfo = RouteUtil.routeToAction(routeMapping);
                    String namespace = actionInfo.getNamespace();
                    String actionName = actionInfo.getActionName();

                    //create action config
                    PackageConfig.Builder packageCfgBuilder = findOrCreatePackage(namespace, packages);
                    ActionConfig actionCfg = createActionConfig(packageCfgBuilder, classInfo.getName(), routeMapping.getMethod().getName(), actionName);
                    packageCfgBuilder.addActionConfig(actionCfg.getName(), actionCfg);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("could not find controllers");
        }

        return packages;
    }

    /**
     * 默认父包
     *
     * @return
     */
    private PackageConfig defaultParentPackage() {
        return configuration.getPackageConfig(defaultParentPackage);
    }

    private PackageConfig.Builder findOrCreatePackage(String namespace, Map<String, PackageConfig.Builder> packages) {
        String packageName = "mapping-default#" + namespace;
        PackageConfig.Builder packageCfgBuilder = packages.get(packageName);
        if (packageCfgBuilder == null) {
            PackageConfig defaultParent = this.defaultParentPackage();
            packageCfgBuilder = new PackageConfig.Builder(packageName);
            packageCfgBuilder.addParent(defaultParent);
            packageCfgBuilder.namespace(namespace);
            packages.put(packageName, packageCfgBuilder);
        }
        return packageCfgBuilder;
    }

    private ActionConfig createActionConfig(PackageConfig.Builder packageConfigBuilder, String className, String actionName) {
        return createActionConfig(packageConfigBuilder, className, null, actionName);
    }

    private ActionConfig createActionConfig(PackageConfig.Builder packageConfigBuilder, String className, String methodName, String actionName) {
        ActionConfig.Builder actionCfgBuilder = new ActionConfig.Builder(packageConfigBuilder.getName(), actionName, className);
        actionCfgBuilder.methodName(methodName);
        return actionCfgBuilder.build();
    }

    @VisibleForTesting
    public static Set<ClassPath.ClassInfo> findControllers(String controllerPackage) throws IOException {
        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());

        Set<ClassPath.ClassInfo> allClasses = classPath.getTopLevelClassesRecursive(controllerPackage);
        Set<ClassPath.ClassInfo> controllers = Sets.filter(allClasses, new Predicate<ClassPath.ClassInfo>() {
            @Override
            public boolean apply(ClassPath.ClassInfo classInfo) {
                //类一定要是 "Controller" 结束
                if (classInfo.getSimpleName().endsWith("Controller")) {
                    log.trace("controller:{}", classInfo.getName());
                    return true;
                }
                return false;
            }
        });
        return controllers;
    }

    @VisibleForTesting
    public static List<RouteMapping> parseController(Class controller) {
        List<RouteMapping> routes = new ArrayList<RouteMapping>();
        Method[] methods = controller.getMethods();
        if (methods != null) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(Route.class)) {
                    Route route = method.getAnnotation(Route.class);
                    if (log.isTraceEnabled()) {
                        //TODO 更好的显示 method
                        log.trace(String.format("route: %s %s%s", prettyMethods(route.method()), route.value(), prettyParams(route.params())));
                    }
                    routes.add(new RouteMapping(route, controller, method));
                }
            }
        }

        return routes;
    }

    private static String prettyParams(String[] params) {
        if (params == null) {
            return "";
        }

        if (params.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("?");
        boolean isFirst = true;
        for (String param : params) {
            if (!isFirst) {
                sb.append("&");
            }
            sb.append(param);
            if (isFirst) {
                isFirst = !isFirst;
            }
        }
        return sb.toString();
    }

    private static String prettyMethods(MethodType[] types) {
        if (types == null || types.length == 0) {
            return MethodType.NONE.toString();
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (MethodType type : types) {
            if (!isFirst) {
                sb.append(" | ");
            }
            sb.append(type.toString());

            if (isFirst) {
                isFirst = !isFirst;
            }
        }
        return sb.toString();
    }

}

