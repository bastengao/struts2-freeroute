package org.apache.struts2.freeroute;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author bastengao
 * @date 12-12-16 22:58
 */
public class DefaultRouteMappingHandler implements RouteMappingHandler {
    private Map<String, RouteMapping> routes = new LinkedHashMap<String, RouteMapping>();

    @Override
    public void put(String key, RouteMapping routeMapping) {
        routes.put(key, routeMapping);
    }

    @Override
    public RouteMapping route(String url) {
        return routes.get(url);
    }
}
