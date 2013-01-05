package com.bastengao.struts2.freeroute;

import com.google.common.annotations.VisibleForTesting;
import com.bastengao.struts2.freeroute.annotation.MethodType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路由工具类
 *
 * @author bastengao
 * @date 12-12-18 23:25
 */
public class RouteUtil {

    //路径变量正则 "/{([a-zA-Z]+)}"
    public static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("/\\{([a-zA-Z]+)\\}");

    private RouteUtil() {
    }

    /**
     * 将路由解析为 action 信息
     * <p/>
     * 同样的路径, 不同的 method 映射的是不同的 action
     * <p/>
     * 如果是静态路由 "/persons/new" 将变成 "/persons/new#method"
     * 如果是动态路由 "/persons/{id}" 将变成 "/persons/__id__#method"
     *
     * @param routeMapping
     * @return
     */
    public static ActionInfo routeToAction(RouteMapping routeMapping) {
        String routePath = routeMapping.getRoute().value();
        routePath = ActionUtil.padSlash(routePath);
        routePath = RouteUtil.flatRoutePath(routePath);

        String namespace = ActionUtil.namespace(routePath);
        String actionName = ActionUtil.actionName(routePath);
        actionName = actionName + "#" + routeMapping.getMethod().getName();
        return new ActionInfo(namespace, actionName);
    }

    /**
     * 是否有 pathVariable
     *
     * @param routePath
     * @return
     */
    public static boolean hasPathVariables(String routePath) {
        Matcher matcher = PATH_VARIABLE_PATTERN.matcher(routePath);
        return matcher.find();
    }

    /**
     * 将路由路径转化为能够匹配此路径请求的正则表达式
     * <p/>
     * TODO 目前只匹配字母,数字和中文
     * TODO 比较粗狂的解决办法就是排除掉 "/" 和 http 协议冲突的字符就可以了
     *
     * @param routePath
     * @return
     */
    public static String toRoutePathPattern(String routePath) {
        // 数字, 英文, 中文
        // 0-9, a-zA-Z, \u4e00-\u9fa5
        // (为什么是 \\\\ ，因为在正则表达式里要表示 \ 需要转义为 \\,
        // 但在 java 里表示 \\, 也需要转义就变成了 \\\\)
        return PATH_VARIABLE_PATTERN.matcher(routePath).replaceAll("/([0-9a-zA-Z\\\\u4e00-\\\\u9fa5]+)");
    }

    /**
     * 返回路由中的变量名
     *
     * @param routePath
     * @return
     */
    public static List<String> pathVariableNames(String routePath) {
        Matcher matcher = PATH_VARIABLE_PATTERN.matcher(routePath);
        //TODO 优化: 变量名不能相同, 未做判断
        List<String> variableNames = new ArrayList<String>();
        while (matcher.find()) {
            variableNames.add(matcher.group(1));
        }
        return variableNames;
    }

    /**
     * 如果 routePath 中有 pathVariable,
     * 例如 "/persons/{id}" 那么将路由转化为 "/persons/__id__"
     * "/persons/{id}/edit" 转化为 "/persons/__id__/edit"
     *
     * @param routePath
     * @return
     */
    @VisibleForTesting
    public static String flatRoutePath(String routePath) {
        StringBuilder flatRoutePath = new StringBuilder();
        //下一次匹配的开始
        int nextStart = 0;
        Matcher matcher = PATH_VARIABLE_PATTERN.matcher(routePath);
        while (matcher.find()) {
            String variableName = matcher.group(1);
            int matchStart = matcher.start(); //此次匹配的开始
            int matchEnd = matcher.end(); //引次匹配的结束

            flatRoutePath.append(routePath.substring(nextStart, matchStart));
            flatRoutePath.append("/__").append(variableName).append("__");

            nextStart = matchEnd;
        }
        flatRoutePath.append(routePath.substring(nextStart));

        return flatRoutePath.toString();
    }

    /**
     * 根据 http method 返回对应的枚举
     * 如果没有匹配的返回 MethodType.NONE
     *
     * @param method
     * @return
     */
    public static MethodType valueOfMethod(String method) {
        try {
            return MethodType.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MethodType.NONE;
        }
    }
}
