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

    /**
     * @param route
     * @param method
     * @deprecated
     */
    public RouteMapping(Route route, Method method) {
        this.route = route;
        this.method = method;
    }

    public RouteMapping(Route route, Class action, Method method) {
        this.route = route;
        this.action = action;
        this.method = method;
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

}
