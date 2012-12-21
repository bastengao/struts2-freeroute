package org.apache.struts2.freeroute;

import org.apache.struts2.freeroute.annotation.Route;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 路由映射
 *
 * @author bastengao
 * @date 12-12-16 20:17
 */
public class RouteMapping {
    //路由
    private Route route;
    //controller
    private Class action;
    //被 @Route 注解的方法
    private Method method;

    // routePath 上是否有 pathVariable
    private boolean hasPathVariables;
    //路径上的变量名
    private List<String> variableNames;
    //匹配请求是否适合此路由的正则
    private Pattern routePathPattern;

    public RouteMapping(Route route, Class action, Method method) {
        this.route = route;
        this.action = action;
        this.method = method;

        String routePath = route.value();
        this.hasPathVariables = RouteUtil.hasPathVariables(routePath);
        if (hasPathVariables) {
            routePathPattern = Pattern.compile(RouteUtil.toRoutePathPattern(routePath));
            variableNames = Collections.unmodifiableList(RouteUtil.pathVariableNames(routePath));
        }
    }

    public Route getRoute() {
        return route;
    }

    public Class getAction() {
        return action;
    }

    public Method getMethod() {
        return method;
    }

    public boolean hasPathVariables() {
        return hasPathVariables;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }

    public Pattern getRoutePathPattern() {
        return routePathPattern;
    }
}
