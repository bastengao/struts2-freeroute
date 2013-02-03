package com.bastengao.struts2.freeroute;

import com.google.common.annotations.VisibleForTesting;

/**
 * 提供处理 action 相关工具方法
 *
 * @author bastengao
 * @date 12-12-17 23:04
 * @since 1.0
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
        if (index == -1) {
            return "";
        }
        return routePath.substring(0, index);
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

    public static String padSlash(String str) {
        if (!str.startsWith("/")) {
            return "/" + str;
        }

        return str;
    }

    /**
     * 去掉尾部的 "/", 如果有
     *
     * @param str
     * @return
     */
    public static String shrinkEndSlash(String str) {
        if (str.endsWith("/")) {
            return str.substring(0, str.length() - 1);
        }

        return str;
    }
}
