package org.apache.struts2.freeroute;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一负责路由的管理，添加与解析
 *
 * @author bastengao
 * @date 12-12-16 22:57
 */
public interface RouteMappingHandler {
    /**
     * 添加新的路由
     *
     * @param routePath
     * @param routeMapping
     */
    public void put(String routePath, RouteMapping routeMapping);

    /**
     * 根据请求返回对应的路由映射, 如果没有返回 null
     *
     * @param request
     * @return
     */
    public RouteMapping route(HttpServletRequest request);
}
