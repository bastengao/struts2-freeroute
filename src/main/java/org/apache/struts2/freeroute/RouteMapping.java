package org.apache.struts2.freeroute;

import org.apache.struts2.freeroute.annotation.Route;

import java.lang.reflect.Method;

/**
 * @author bastengao
 * @date 12-12-16 20:17
 */
public class RouteMapping {
    private Route route;
    private Class action;
    private Method method;

    // routePath 上是否有 pathVariable
    private boolean hasPathVariables;

    public RouteMapping(Route route, Class action, Method method) {
        this.route = route;
        this.action = action;
        this.method = method;

        hasPathVariables = RouteUtil.hasPathVariables(route.value());
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
}
