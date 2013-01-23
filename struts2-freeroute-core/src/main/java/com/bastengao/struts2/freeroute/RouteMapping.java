package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.ContentBase;
import com.bastengao.struts2.freeroute.annotation.CookieValue;
import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 路由映射
 *
 * @author bastengao
 * @date 12-12-16 20:17
 * @since 1.0
 */
public class RouteMapping {
    //可能有，可能没有
    private ContentBase contentBase;
    //路由 TODO 弱化 @Route 作用
    private Route route;
    // route path. 原始路由路径
    private String routePath;
    // http methods
    private MethodType[] httpMethods;
    // http params
    private String[] httpParams;

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


    // 被 @CookieValue 注解的 field
    private Map<CookieValue, Field> cookieValues;


    public RouteMapping(Route route, Class action, Method method) {
        this.route = route;
        this.action = action;
        this.method = method;

        this.routePath = route.value();
        this.httpMethods = route.method();
        this.httpParams = route.params();

        initParams();
        initPathVariables();
        initCookieValues();
    }

    public RouteMapping(ContentBase contentBase, Route route, Class action, Method method) {
        this(route, action, method);
        this.contentBase = contentBase;
    }

    /**
     * 初始化 params
     */
    private void initParams() {
        ArrayList<Param> params = new ArrayList<Param>();
        for (String param : route.params()) {
            params.add(new Param(param));
        }
        params.trimToSize(); // 减少空间
        this.params = Collections.unmodifiableList(params);
    }

    /**
     * 初始化 pathVariables
     */
    private void initPathVariables() {
        String routePath = route.value();
        this.hasPathVariables = RouteUtil.hasPathVariables(routePath);
        if (hasPathVariables) {
            routePathPattern = Pattern.compile(RouteUtil.toRoutePathPattern(routePath));
            variableNames = Collections.unmodifiableList(RouteUtil.pathVariableNames(routePath));
        }
    }

    /**
     * 初始化 cookie
     */
    private void initCookieValues() {
        cookieValues = new HashMap<CookieValue, Field>();
        for (Field field : action.getDeclaredFields()) {
            if (ReflectUtil.isAnnotationPresentOfField(field, CookieValue.class)) {
                CookieValue cookieValue = ReflectUtil.getAnnotationOfField(field, CookieValue.class);
                cookieValues.put(cookieValue, field);
            }
        }
        cookieValues = Collections.unmodifiableMap(cookieValues);
    }


    public ContentBase getContentBase() {
        return contentBase;
    }

    /**
     * TODO 弱化 @Route 作用
     *
     * @return
     * @deprecated
     */
    public Route getRoute() {
        return route;
    }

    public String getRoutePath(){
        return routePath;
    }

    /**
     * TODO clone value
     *
     * @return
     */
    public String[] getHttpParams() {
        return httpParams;
    }

    /**
     * TODO clone value
     *
     * @return
     */
    public MethodType[] getHttpMethods() {
        return httpMethods;
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

    public Map<CookieValue, Field> getCookieValues() {
        return cookieValues;
    }

    /**
     * 表示一个 http param 表达式
     */
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
