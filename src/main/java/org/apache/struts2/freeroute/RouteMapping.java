package org.apache.struts2.freeroute;

import org.apache.struts2.freeroute.annotation.Route;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

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
    private List<String> variableNames;
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
