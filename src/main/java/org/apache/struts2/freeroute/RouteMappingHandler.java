package org.apache.struts2.freeroute;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bastengao
 * @date 12-12-16 22:57
 */
public interface RouteMappingHandler {
    public void put(String key, RouteMapping routeMapping);

    /**
     * 根据请求返回对应的路由映射, 如果没有返回 null
     *
     * @param request
     * @return
     */
    public RouteMapping route(HttpServletRequest request);
}
