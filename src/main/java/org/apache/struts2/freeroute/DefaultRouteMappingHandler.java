package org.apache.struts2.freeroute;


import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
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
     * @param key
     * @param routeMapping
     */
    @Override
    public void put(String key, RouteMapping routeMapping) {
        if (routeMapping.hasPathVariables()) {
            //TODO 写死的正则
            dynamicRoutes.put(Pattern.compile(routeMapping.getRoute().value()), routeMapping);
        } else {
            staticRoutes.put(key, routeMapping);
        }
    }

    @Override
    public RouteMapping route(HttpServletRequest request) {
        //三个依据判断是否有对应的路由映射
        //1. servletPath ( 其中可以包括 pathVariable 的匹配 )
        //2. http method
        //3. 特定的 param
        String servletPath = request.getServletPath();
        if (staticRoutes.containsKey(servletPath)) {
            return staticRoutes.get(servletPath);
        }

        // TODO try dynamicRoutes
        return null;
    }
}
