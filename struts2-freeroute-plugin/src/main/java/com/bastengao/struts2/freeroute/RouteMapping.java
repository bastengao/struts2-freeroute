package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.ContentBase;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 路由映射
 *
 * @author bastengao
 * @date 12-12-16 20:17
 */
public class RouteMapping {
    //可能有，可能没有
    private ContentBase contentBase;
    //路由
    private Route route;
    // http 参数规则
    List<Param> params;
    //controller
    private Class action;
    //被 @Route 注解的方法
    private Method method;

    // routePath 上是否有 pathVariable
    private boolean hasPathVariables;
    //路径上的变量名
    private List<String> variableNames;
    //匹配请求是否适合此路由的正则
    private Pattern routePathPattern;

    public RouteMapping(Route route, Class action, Method method) {
        this.route = route;
        this.action = action;
        this.method = method;

        ArrayList<Param> params = new ArrayList<Param>();
        for (String param : route.params()) {
            params.add(new Param(param));
        }
        params.trimToSize(); // 减少空间
        this.params = Collections.unmodifiableList(params);


        String routePath = route.value();
        this.hasPathVariables = RouteUtil.hasPathVariables(routePath);
        if (hasPathVariables) {
            routePathPattern = Pattern.compile(RouteUtil.toRoutePathPattern(routePath));
            variableNames = Collections.unmodifiableList(RouteUtil.pathVariableNames(routePath));
        }
    }

    public RouteMapping(ContentBase contentBase, Route route, Class action, Method method) {
        this(route, action, method);
        this.contentBase = contentBase;
    }

    public ContentBase getContentBase() {
        return contentBase;
    }

    public Route getRoute() {
        return route;
    }

    public List<Param> getParams() {
        return params;
    }

    public Class getAction() {
        return action;
    }

    public Method getMethod() {
        return method;
    }

    public boolean hasPathVariables() {
        return hasPathVariables;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }

    public Pattern getRoutePathPattern() {
        return routePathPattern;
    }

    static class Param {
        private String paramName;

        /**
         * 是否需要存在性检查
         */
        private boolean needForExistingCheck = false;

        /**
         * 是否需要表达式检查(等于或者不等于)
         */
        private boolean needForEqualExpressionCheck = false;
        /**
         * 相等或者不相等
         */
        private boolean equalOrNot = true;
        /**
         * 比较的值
         */
        private String paramValue;

        public Param(String param) {
            //TODO 分析表达示这还不够严谨
            //是否有表达示
            if (param.contains("=")) {
                //需要表达式检查
                this.needForEqualExpressionCheck = true;
                //不相等表达示
                if (param.contains("!=")) {
                    //不相等
                    equalOrNot = false;
                    paramName = param.split("\\!\\=")[0];
                    paramValue = param.split("\\!\\=")[1];
                }
                //相等表达示
                else if (param.contains("=")) {
                    //相等
                    equalOrNot = true;
                    paramName = param.split("=")[0];
                    paramValue = param.split("=")[1];
                }
            } else {
                this.needForExistingCheck = true;
                this.paramName = param;
            }
        }

        /**
         * 查看当前请求是否匹配此 param
         *
         * @param request
         * @param params
         * @return
         */
        public boolean match(HttpServletRequest request, Set<String> params) {
            if (needForExistingCheck) {
                return params.contains(paramName);
            }

            if (needForEqualExpressionCheck) {
                if (params.contains(paramName)) {
                    String value = request.getParameter(paramName);
                    if (!Strings.isNullOrEmpty(value)) {
                        return equalOrNot == (paramValue.equals(value));
                    }
                }
            }

            return false;
        }

    }
}
