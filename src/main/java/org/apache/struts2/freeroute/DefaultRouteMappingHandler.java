package org.apache.struts2.freeroute;


import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 处理所有的路由信息
 *
 * @author bastengao
 * @date 12-12-16 22:58
 */
public class DefaultRouteMappingHandler implements RouteMappingHandler {
    /**
     * 可将 mapping 分成两种，一种是静态的 直接通过 Map 就可以快速找到路由；
     * 另一种是有 pathVariable 的，需要通过正则匹配查找路由
     */
    private Map<String, RouteMapping> staticRoutes = new LinkedHashMap<String, RouteMapping>();

    /**
     * routePath 中包括 pathVariable 中的路由映射
     */
    private Map<Pattern, RouteMapping> dynamicRoutes = new LinkedHashMap<Pattern, RouteMapping>();

    /**
     * 默认 key 是 @Route.value 的值
     *
     * @param flattedRoutePath
     * @param routeMapping
     */
    @Override
    public void put(String flattedRoutePath, RouteMapping routeMapping) {
        if (routeMapping.hasPathVariables()) {
            // 正则 => 路由
            dynamicRoutes.put(routeMapping.getRoutePathPattern(), routeMapping);
        } else {
            staticRoutes.put(flattedRoutePath, routeMapping);
        }
    }

    @Override
    public RouteMapping route(HttpServletRequest request) {
        //三个依据判断是否有对应的路由映射
        //1. servletPath ( 其中可以包括 pathVariable 的匹配 )
        //2. http method
        //3. 特定的 param

        // TODO 路径匹配后，继续匹配 method  和 param

        String servletPath = request.getServletPath();
        if (staticRoutes.containsKey(servletPath)) {
            return staticRoutes.get(servletPath);
        }
        // try dynamicRoutes
        for(Pattern pattern: dynamicRoutes.keySet()){
            if(pattern.matcher(servletPath).matches()){
                return dynamicRoutes.get(pattern);
            }
        }
        return null;
    }
}
