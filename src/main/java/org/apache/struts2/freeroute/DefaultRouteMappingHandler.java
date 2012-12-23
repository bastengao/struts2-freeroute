package org.apache.struts2.freeroute;


import com.google.common.collect.ArrayListMultimap;
import org.apache.struts2.freeroute.annotation.MethodType;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 处理所有的路由信息
 * <p/>
 * 可将 mapping 分成两种，一种是静态的 直接通过 Map 就可以快速找到路由；
 * 另一种是有 pathVariable 的，需要通过正则匹配查找路由
 *
 * @author bastengao
 * @date 12-12-16 22:58
 */
public class DefaultRouteMappingHandler implements RouteMappingHandler {
    /**
     * 静态路由
     */
    private ArrayListMultimap<String, RouteMapping> staticRoutes = ArrayListMultimap.create();

    /**
     * routePath 中包括 pathVariable 中的路由映射
     */
    //private Map<String, RouteMapping> dynamicRoutes = new LinkedHashMap<String, RouteMapping>();
    private ArrayListMultimap<String, RouteMapping> dynamicRoutes = ArrayListMultimap.create();
    private Map<String, Pattern> dynamicRoutesPattern = new HashMap<String, Pattern>();

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
            dynamicRoutes.put(routeMapping.getRoutePathPattern().pattern(), routeMapping);
            dynamicRoutesPattern.put(routeMapping.getRoutePathPattern().pattern(), routeMapping.getRoutePathPattern());
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

        String servletPath = request.getServletPath();

        // 路径匹配后，继续匹配 method  和 param
        if (staticRoutes.containsKey(servletPath)) {
            //匹配 servletPath 的路由
            List<RouteMapping> matchPathRouteMapping = staticRoutes.get(servletPath);
            return findMaxWeight(request, matchPathRouteMapping);
        }

        // try dynamicRoutes
        for (Map.Entry<String, Pattern> patternEntry : dynamicRoutesPattern.entrySet()) {
            if (patternEntry.getValue().matcher(servletPath).matches()) {
                //匹配 servletPath 的路由
                List<RouteMapping> matchPathRouteMappings = dynamicRoutes.get(patternEntry.getKey());
                return findMaxWeight(request, matchPathRouteMappings);
            }
        }
        return null;
    }

    /**
     * 根据已经匹配 servletPath 的路由集合找出最匹配的路由
     *
     * @param request
     * @param routeMappings
     * @return
     */
    private RouteMapping findMaxWeight(HttpServletRequest request, List<RouteMapping> routeMappings) {
        int maxWeight = -1;
        RouteMapping maxWeightRoute = null;
        for (RouteMapping routeMapping : routeMappings) {
            int weight = weightOfRoute(request, routeMapping);
            if (weight > 0) {
                if (weight > maxWeight) {
                    maxWeight = weight;
                    maxWeightRoute = routeMapping;
                }
            }
        }
        return maxWeightRoute;
    }

    /**
     * 返回请求与匹配的路由的权重. 如果不匹配返回小于 0 的值，如果匹配返回权重值。
     * 其中 method 的权重比 param 权重高
     * weight = method(GET, POST, PUT, DELETE) * 20  +  param * 1
     * weight = method(NONE) * 10  +  param * 1
     *
     * @param request
     * @param routeMapping
     * @return
     */
    private static int weightOfRoute(HttpServletRequest request, RouteMapping routeMapping) {
        // 测试 method 是否匹配，
        if (!isMatchMethod(request, routeMapping)) {
            return -1;
        }

        // 测试是否匹配 param
        if (!isMatchParams(request, routeMapping)) {
            return -1;
        }

        // 计算 method 权重
        int methodWeight = 0;
        // MethodType.NONE 权重为 10, 其他为 20
        if (routeMapping.getRoute().method() == MethodType.NONE) {
            methodWeight = 10;
        } else {
            methodWeight = 20;
        }

        //TODO 添加 param 处理逻辑
        // 计算 param 权重
        int paramWeight = 0;
        return methodWeight + paramWeight;
    }

    private static boolean isMatchMethod(HttpServletRequest request, RouteMapping routeMapping) {
        if (routeMapping.getRoute().method() == MethodType.NONE) {
            return true;
        }

        MethodType methodType = RouteUtil.valueOfMethod(request.getMethod());
        if (methodType == null) {
            return false;
        }

        return routeMapping.getRoute().method() == methodType;
    }

    private static boolean isMatchParams(HttpServletRequest request, RouteMapping routeMapping) {
        //TODO
        return true;
    }
}
