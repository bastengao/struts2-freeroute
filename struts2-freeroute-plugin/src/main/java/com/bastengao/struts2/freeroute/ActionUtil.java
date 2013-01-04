package com.bastengao.struts2.freeroute;

import com.google.common.annotations.VisibleForTesting;

/**
 * @author bastengao
 * @date 12-12-17 23:04
 */
@VisibleForTesting
public class ActionUtil {
    /**
     * 根据路由路径获取 namespace
     *
     * @param routePath
     * @return
     */
    @VisibleForTesting
    public static String namespace(String routePath) {
        int index = routePath.lastIndexOf("/");
        if (index != -1) {
            return routePath.substring(0, index);
        }
        return "";
    }

    /**
     * 根据路由路径获取 action name
     *
     * @param routePath
     * @return
     */
    @VisibleForTesting
    public static String actionName(String routePath) {
        int index = routePath.lastIndexOf("/");
        if (index == -1) {
            return routePath;
        }
        return routePath.substring(index + 1);
    }

    @VisibleForTesting
    public static String padSlash(String str) {
        if (!str.startsWith("/")) {
            return "/" + str;
        }

        return str;
    }
}
