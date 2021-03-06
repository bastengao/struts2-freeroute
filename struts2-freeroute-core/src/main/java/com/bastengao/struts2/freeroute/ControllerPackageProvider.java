package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.ContentBase;
import com.bastengao.struts2.freeroute.annotation.ControllerPackage;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.PackageProvider;
import com.opensymphony.xwork2.config.entities.*;
import com.opensymphony.xwork2.config.providers.InterceptorBuilder;
import com.opensymphony.xwork2.inject.Inject;
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
 * @since 1.0
 */
public class ControllerPackageProvider implements PackageProvider {
    private static final Logger log = LoggerFactory.getLogger(ControllerPackageProvider.class);
    // package name prefix
    public static final String FREEROUTE_DEFAULT = "freeroute-default#";

    private Configuration configuration;
    private ObjectFactory objectFactory;
    private RouteMappingHandler routeMappingHandler;

    // controller 所在的包
    private String controllerPackage;

    // controller 后缀
    private Set<String> controllerSuffixes;

    // 默认父包 (如果没有配置，默认为 "struts-default")
    private String defaultParentPackage;


    // 是否 struts2-spring-plugin 被加载, 如果存在将 action class name 转换为 spring bean name
    private boolean hasSpringPlugin = false;

    @Inject("routeMappingHandler")
    private void setRouteMappingHandler(RouteMappingHandler routeMappingHandler) {
        log.trace("routeMappingHandler:{}", routeMappingHandler);
        this.routeMappingHandler = routeMappingHandler;
    }

    @Inject(value = "struts.freeroute.controllerPackage", required = true)
    private void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    @Inject(value = "struts.freeroute.controllerSuffixes", required = true)
    private void setControllerSuffixes(String controllerSuffixes) {

        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        this.controllerSuffixes = Sets.newHashSet(splitter.split(controllerSuffixes));
    }

    @Inject(value = "struts.freeroute.defaultParentPackage", required = true)
    private void setDefaultParentPackage(String defaultParentPackage) {
        this.defaultParentPackage = defaultParentPackage;
    }

    @Inject
    private void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    /**
     * 注意，如果不存在 struts2-spring-plugin 那么此方法不会被调用
     *
     * @param objectFactory
     */
    @Inject(value = "spring", required = false)
    private void setStrutsSpringObjectFactory(ObjectFactory objectFactory) {
        hasSpringPlugin = true;
    }

    @Override
    public void init(Configuration configuration) throws ConfigurationException {
        log.debug("init");
        log.debug("struts2-spring-plugin: {}", hasSpringPlugin);
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

        Collection<PackageConfig.Builder> packages = createPackageConfig();
        for (PackageConfig.Builder packageBuilder : packages) {
            configuration.addPackageConfig(packageBuilder.getName(), packageBuilder.build());
        }
    }

    /**
     * 创建 Package 配置与  Action 配置
     *
     * @return 返回 Package 配置集合
     */
    private Collection<PackageConfig.Builder> createPackageConfig() {
        // packageName => PackageConfig.Builder
        List<PackageConfig.Builder> packages = new LinkedList<PackageConfig.Builder>();

        try {
            //分析所有的 "Controller"
            for (ClassPath.ClassInfo classInfo : findControllers(controllerPackage, controllerSuffixes)) {
                Class<?> controllerClass = classInfo.load();
                PackageConfig parentPackage = findParentPackage(controllerClass);

                // namespace => PackageConfig.Builder
                Map<String, PackageConfig.Builder> controllerPackages = new HashMap<String, PackageConfig.Builder>();
                List<RouteMapping> routeMappings = parseController(controllerClass);
                for (RouteMapping routeMapping : routeMappings) {
                    //将路由转换为 action
                    ActionInfo actionInfo = routeMapping.toAction();
                    String namespace = actionInfo.getNamespace();

                    //create action config
                    PackageConfig.Builder packageCfgBuilder = findOrCreatePackage(controllerClass.getName(), namespace, parentPackage, controllerPackages);
                    ActionConfig actionCfg = createActionConfig(parentPackage, packageCfgBuilder, actionInfo, routeMapping);
                    packageCfgBuilder.addActionConfig(actionCfg.getName(), actionCfg);

                    //添加路由映射
                    routeMappingHandler.put(routeMapping, actionCfg);
                }
                packages.addAll(controllerPackages.values());
            }
        } catch (IOException e) {
            throw new IllegalStateException("could not find controllers");
        }

        return packages;
    }

    /**
     * 返回父包
     *
     * @param controllerClass
     * @return
     * @since 1.0.2
     */
    private PackageConfig findParentPackage(Class<?> controllerClass) {
        ControllerPackage pkg = ReflectUtil.getAnnotation(controllerClass, ControllerPackage.class);
        // 父包
        PackageConfig parentPackage = null;
        if (pkg == null) {
            parentPackage = this.defaultParentPackage();

            if(parentPackage == null){
                throw new IllegalStateException("defaultParentPackage not found: " + this.defaultParentPackage);
            }
        } else {
            parentPackage = this.configuration.getPackageConfig(pkg.parent());
        }
        return parentPackage;
    }

    /**
     * 默认父包
     *
     * @return
     */
    private PackageConfig defaultParentPackage() {
        return configuration.getPackageConfig(defaultParentPackage);
    }


    /**
     * 查找 Package，如果不存在则创建
     *
     * @param namespace
     * @param packages
     * @return
     */
    private PackageConfig.Builder findOrCreatePackage(String namespace, Map<String, PackageConfig.Builder> packages) {
        String packageName = FREEROUTE_DEFAULT + namespace;
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

    /**
     * 查找 Package，如果不存在则创建
     *
     * @param namespace
     * @param packages
     * @return
     */
    private PackageConfig.Builder findOrCreatePackage(String controllerClassName, String namespace, PackageConfig parentPackage, Map<String, PackageConfig.Builder> packages) {
        // packageName = controller.className + namespace
        String packageName = FREEROUTE_DEFAULT + controllerClassName + "#" + namespace;
        PackageConfig.Builder packageCfgBuilder = packages.get(namespace);
        if (packageCfgBuilder == null) {
            packageCfgBuilder = new PackageConfig.Builder(packageName);
            packageCfgBuilder.addParent(parentPackage);
            packageCfgBuilder.namespace(namespace);
            packages.put(namespace, packageCfgBuilder);
        }
        return packageCfgBuilder;
    }


    /**
     * 创建 action 配置
     *
     * @param parentConfig
     * @param packageCfgBuilder
     * @param actionInfo
     * @param routeMapping
     * @return
     */
    private ActionConfig createActionConfig(PackageConfig parentConfig, PackageConfig.Builder packageCfgBuilder, ActionInfo actionInfo, RouteMapping routeMapping) {
        String actionName = actionInfo.getActionName();

        //create action config
        String actionClass = routeMapping.getAction().getName();
        String actionMethodName = routeMapping.getMethod().getName();
        ActionConfig.Builder actionCfgBuilder = createActionConfig(packageCfgBuilder, actionClass, actionMethodName, actionName);

        // 配置拦截器
        for (String interceptor : routeMapping.getInterceptors()) {
            actionCfgBuilder.addInterceptors(getInterceptorMappings(parentConfig, interceptor));
        }
        return actionCfgBuilder.build();
    }

    private ActionConfig createActionConfig(PackageConfig.Builder packageConfigBuilder, String className, String actionName) {
        return createActionConfig(packageConfigBuilder, className, null, actionName).build();
    }

    protected ActionConfig.Builder createActionConfig(PackageConfig.Builder packageConfigBuilder, String className, String methodName, String actionName) {
        ActionConfig.Builder actionCfgBuilder = new ActionConfig.Builder(packageConfigBuilder.getName(), actionName, className);
        actionCfgBuilder.methodName(methodName);
        return actionCfgBuilder;
    }

    /**
     * 返回父包下指定拦截器名称对应的 InterceptorMapping
     *
     * @param parentConfig
     * @param interceptorName
     * @return
     * @since 1.0.2
     */
    private List<InterceptorMapping> getInterceptorMappings(PackageConfig parentConfig, String interceptorName) {
        Object interceptorConfig = parentConfig.getInterceptorConfig(interceptorName);
        Map<String, String> params = null;
        // 如果是一般拦截器
        if (interceptorConfig instanceof InterceptorConfig) {
            params = ((InterceptorConfig) interceptorConfig).getParams();
        }
        // 如果一拦截器栈
        else if (interceptorConfig instanceof InterceptorStackConfig) {
            params = new LinkedHashMap<String, String>();
        }

        return InterceptorBuilder.constructInterceptorReference(parentConfig,
                interceptorName,
                params,
                parentConfig.getLocation(),
                objectFactory);
    }

    /**
     * 在指定的包下查找带有指定后缀的 class
     *
     * @param controllerPackage
     * @param controllerSuffixes
     * @return
     * @throws java.io.IOException
     */
    @VisibleForTesting
    static Set<ClassPath.ClassInfo> findControllers(String controllerPackage, final Set<String> controllerSuffixes) throws IOException {
        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());

        Set<ClassPath.ClassInfo> allClasses = classPath.getTopLevelClassesRecursive(controllerPackage);
        Set<ClassPath.ClassInfo> controllers = Sets.filter(allClasses, new Predicate<ClassPath.ClassInfo>() {
            @Override
            public boolean apply(ClassPath.ClassInfo classInfo) {
                for (String controllerSuffix : controllerSuffixes) {
                    // 判断是否是指定后缀结束
                    if (classInfo.getSimpleName().endsWith(controllerSuffix)) {
                        log.trace("controller:{}", classInfo.getName());
                        return true;
                    }
                }
                return false;
            }
        });
        return controllers;
    }

    /**
     * 解析 Controller 并返回路由信息
     *
     * @param controller
     * @return
     */
    @VisibleForTesting
    static List<RouteMapping> parseController(Class controller) {
        List<RouteMapping> routes = new ArrayList<RouteMapping>();
        //遍历 Controller 的所有方法
        Method[] methods = controller.getMethods();
        if (methods == null) {
            return routes;
        }

        ContentBase contentBase = ReflectUtil.getAnnotation(controller, ContentBase.class);
        Route controllerRoute = ReflectUtil.getAnnotation(controller, Route.class);
        for (Method method : methods) {
            //查看是否有 @Route 注解, 如果有则加到路由列表中
            if (method.isAnnotationPresent(Route.class)) {
                Route methodRoute = method.getAnnotation(Route.class);
                RouteMapping routeMapping = new RouteMapping(contentBase, controllerRoute, methodRoute, controller, method);
                routes.add(routeMapping);

                if (log.isTraceEnabled()) {
                    log.trace("route: {}", routeMapping.prettyPath());
                }
            }
        }

        return routes;
    }

}