package org.apache.struts2.freeroute;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        // "/{[a-zA-Z]+}"
        //TODO 这里用一种比较简单的办法, 应该使用正则
        /**
         if (routePath.contains("{") && routePath.contains("}")) {
         return true;
         }
         */
        Pattern pattern = Pattern.compile("/\\{[a-za-z]+\\}");
        Matcher matcher = pattern.matcher(routePath);
        return matcher.find();
    }

    /**
     * 将路由路径转化为能够匹配此路径请求的正则表达式
     * 如：
     * "/persons/{id}" => "/persons/([a-zA-Z0-9]+)"
     * "/persons/{id}/edit/{name}" => "/persons/([a-zA-Z0-9]+)/edit/([a-zA-Z0-9]+)"
     * <p/>
     * TODO 目前只匹配字母和数字
     *
     * @param routePath
     * @return
     */
    public static String toRoutePathPattern(String routePath) {
        Pattern pattern = Pattern.compile("/\\{([a-zA-Z]+)\\}");
        Matcher matcher = pattern.matcher(routePath);
        List<String> variableNames = new ArrayList<String>();
        while (matcher.find()) {
            variableNames.add(matcher.group(1));
        }
        matcher.reset();
        String result = routePath;
        for (String variableName : variableNames) {
            result = pattern.matcher(result).replaceFirst(String.format("/([a-zA-Z0-9]+)", variableName));
        }
        return result;
    }

    public static List<String> pathVariableNames(String routePath){
        Pattern pattern = Pattern.compile("/\\{([a-zA-Z]+)\\}");
        Matcher matcher = pattern.matcher(routePath);
        List<String> variableNames = new ArrayList<String>();
        while (matcher.find()) {
            variableNames.add(matcher.group(1));
        }
        return  variableNames;
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
