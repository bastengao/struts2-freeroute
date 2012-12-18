package org.apache.struts2.freeroute;

import org.apache.struts2.freeroute.annotation.Route;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.PackageProvider;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @author bastengao
 * @date 12-12-16 13:56
 */
public class MyPackageProvider implements PackageProvider {
    private static final Logger log = LoggerFactory.getLogger(MyPackageProvider.class);

    private Configuration configuration;
    private RouteMappingHandler routeMappingHandler;

    @Inject("routeMappingHandler")
    private void setRouteMappingHandler(RouteMappingHandler routeMappingHandler) {
        log.trace("routeMappingHandler:{}", routeMappingHandler);
        this.routeMappingHandler = routeMappingHandler;
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
            for (ClassPath.ClassInfo classInfo : findControllers()) {
                List<RouteMapping> routeMappings = parseController(classInfo.load());
                for (RouteMapping routeMapping : routeMappings) {
                    String routePath = routeMapping.getRoute().value();
                    routePath = ActionUtil.padSlash(routePath);
                    routePath = RouteUtil.flatRoutePath(routePath);

                    /**
                     * 如果 routePath 中有 pathVariable,
                     * 例如 "/persons/{id}" 那么将路由转化为 "/persons/__id__"
                     * "/persons/{id}/edit" 转化为 "/persons/__id__/edit"
                     */

                    //添加路由映射
                    routeMappingHandler.put(routePath, routeMapping);

                    //create action config
                    String namespace = ActionUtil.namespace(routePath);
                    String actionName = ActionUtil.actionName(routePath);
                    PackageConfig.Builder packageCfgBuilder = findOrCreatePackage(namespace, packages);
                    ActionConfig actionCfg = createActionConfig(packageCfgBuilder, classInfo.getName(), routeMapping.getMethod().getName(), actionName);
                    packageCfgBuilder.addActionConfig(actionCfg.getName(), actionCfg);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("could not find controllers");
        }

        PackageConfig.Builder packageCfgBuilder = findOrCreatePackage("", packages);
        ActionConfig actionConfig = createActionConfig(packageCfgBuilder, "", "hello-test");
        packageCfgBuilder.addActionConfig(actionConfig.getName(), actionConfig);

        return packages;
    }

    /**
     * 默认父包
     *
     * @return
     */
    private PackageConfig defaultParentPackage() {
        return configuration.getPackageConfig("struts-default");
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

        //TODO 去掉默认的写死页面
        ResultConfig.Builder resultBuilder = new ResultConfig.Builder("success", "org.apache.struts2.dispatcher.ServletDispatcherResult");
        resultBuilder.addParam("location", "/pages/content.html");
        //actionCfgBuilder.addResultConfig(resultBuilder.build());

        return actionCfgBuilder.build();
    }

    @VisibleForTesting
    public static Set<ClassPath.ClassInfo> findControllers() throws IOException {
        String controllerPackage = "com.gaohui.action";
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
                    log.trace("route:{}", route.value());
                    routes.add(new RouteMapping(route, controller, method));
                }
            }
        }

        return routes;
    }

    /**
     * 根据路由路径获取 namespace
     *
     * @param routePath
     * @return
     */
    @VisibleForTesting
    public static String namespace(String routePath) {
        int index = routePath.lastIndexOf("/");
        if (index != -1) {
            return routePath.substring(0, index);
        }
        return "";
    }

    /**
     * 根据路由路径获取 action name
     *
     * @param routePath
     * @return
     */
    @VisibleForTesting
    public static String actionName(String routePath) {
        int index = routePath.lastIndexOf("/");
        if (index == -1) {
            return routePath;
        }
        return routePath.substring(index + 1);
    }

    @VisibleForTesting
    public static String padSlash(String str) {
        if (!str.startsWith("/")) {
            return "/" + str;
        }

        return str;
    }

}

