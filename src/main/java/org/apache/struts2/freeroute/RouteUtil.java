package org.apache.struts2.freeroute;

/**
 * @author bastengao
 * @date 12-12-18 23:25
 */
public class RouteUtil {
    private RouteUtil() {
    }

    /**
     * 是否有 pathVariable
     *
     * @param routePath
     * @return
     */
    public static boolean hasPathVariables(String routePath) {
        // "{[a-zA-Z]+}"
        //TODO 这里用一种比较简单的办法, 应该使用正则
        if (routePath.contains("{") && routePath.contains("}")) {
            return true;
        }
        return false;
    }

    /**
     * 如果 routePath 中有 pathVariable,
     * 例如 "/persons/{id}" 那么将路由转化为 "/persons/__id__"
     * "/persons/{id}/edit" 转化为 "/persons/__id__/edit"
     *
     * @param routePath
     * @return
     */
    public static String flatRoutePath(String routePath) {
        //TODO 这里用一种比较简单的办法
        routePath = routePath.replaceAll("\\{", "__");
        routePath = routePath.replaceAll("}", "__");
        return routePath;
    }
}
