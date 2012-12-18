package org.apache.struts2.freeroute;

/**
 * @author bastengao
 * @date 12-12-16 22:57
 */
public interface RouteMappingHandler {
    public void put(String key, RouteMapping routeMapping);

    /**
     * @param url
     * @return
     */
    public RouteMapping route(String url);
}
