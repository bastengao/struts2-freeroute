package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.CookieValue;
import com.google.common.base.Strings;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 重写 struts 对新来的请求到 ActionMapping 的处理， 前置加入 freeroute 的逻辑，
 * 处理不了的交给父类处理(不影响 struts 本身之前的逻辑).
 * <p/>
 * 针对当前访问的 request 解析为合适的 ActionMapping
 *
 * @author bastengao
 * @date 12-12-16 00:11
 * @since 1.0
 */
public class DefaultActionMapper extends org.apache.struts2.dispatcher.mapper.DefaultActionMapper {
    private static final Logger log = LoggerFactory.getLogger(DefaultActionMapper.class);

    private RouteMappingHandler routeMappingHandler;

    @Inject("routeMappingHandler")
    public void setRouteMappingHandler(RouteMappingHandler routeMappingHandler) {
        log.trace("routeMappingHandler:{}", routeMappingHandler);
        this.routeMappingHandler = routeMappingHandler;
    }

    @Override
    public ActionMapping getMapping(javax.servlet.http.HttpServletRequest request, ConfigurationManager configManager) {
        if (log.isDebugEnabled()) {
            String requestInfo = request.getMethod() + "    " + request.getServletPath();
            if (!Strings.isNullOrEmpty(request.getQueryString())) {
                requestInfo += "?" + request.getQueryString();
            }
            log.debug(requestInfo);
        }


        // 解析请求，并寻找合适的路由，如果没有则接给父类处理
        ActionMapping actionMapping = parseAndFindRouteMapping(request);
        if (actionMapping != null) {
            return actionMapping;
        }


        return super.getMapping(request, configManager);
    }

    /**
     * 解析并查找对应的路由, 同时返回 ActionMapping
     *
     * @param request
     * @return
     */
    private ActionMapping parseAndFindRouteMapping(HttpServletRequest request) {
        RouteMapping routeMapping = routeMappingHandler.route(request);

        if (routeMapping == null) {
            return null;
        }
        log.debug("routeMapping:{}", routeMapping);

        // 这里的的处理步骤与 ControllerPackageProvider 处理一致
        ActionInfo actionInfo = RouteUtil.routeToAction(routeMapping);
        String namespace = actionInfo.getNamespace();
        // 有可能 namespace 为 "", 所以将其变成 "/"
        namespace = ActionUtil.padSlash(namespace);
        String actionName = actionInfo.getActionName();

        return createActionMapping(namespace, actionName,routeMapping, request);
    }

    /**
     * 创建对应的 ActionMapping, 并初始化 ActionMapping.params
     *
     * @param namespace
     * @param actionName
     * @param routeMapping
     * @param request
     * @return
     */
    private static ActionMapping createActionMapping(String namespace, String actionName, RouteMapping routeMapping, HttpServletRequest request) {
        ActionMapping actionMapping = new ActionMapping();
        actionMapping.setNamespace(namespace);
        actionMapping.setName(actionName);
        // 设置 controller 的调用方法
        actionMapping.setMethod(routeMapping.getMethod().getName());

        setParamsByCookieValues(actionMapping, routeMapping, request);
        setParamsByPathVariables(actionMapping, routeMapping, request);

        return actionMapping;
    }

    /**
     * 通过 cookie 设置 params
     *
     * @param actionMapping
     * @param routeMapping
     * @param request
     */
    private static void setParamsByCookieValues(ActionMapping actionMapping, RouteMapping routeMapping, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        if (routeMapping.getCookieValues().isEmpty()) {
            return;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        for (Map.Entry<CookieValue, Field> entry : routeMapping.getCookieValues().entrySet()) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(entry.getKey().value())) {
                    params.put(entry.getValue().getName(), cookie.getValue());
                }
            }
        }

        if (actionMapping.getParams() == null) {
            actionMapping.setParams(params);
        } else {
            actionMapping.getParams().putAll(params);
        }
    }

    /**
     * 设置路径变量的值到 action 的 params 中, 然后 struts 会将 params 应用到对应的 action 的属性上(setter)
     *
     * @param actionMapping
     * @param routeMapping
     * @param request
     */
    private static void setParamsByPathVariables(ActionMapping actionMapping, RouteMapping routeMapping, HttpServletRequest request) {
        if (routeMapping.hasPathVariables()) {
            Map<String, Object> params = new HashMap<String, Object>();
            String servletPath = request.getServletPath();
            Matcher matcher = routeMapping.getRoutePathPattern().matcher(servletPath);
            //路径变量, 顺序与路径一致
            List<String> pathValues = new ArrayList<String>();
            //只匹配一次,  完成匹配
            if (matcher.find()) {
                int groupCount = matcher.groupCount();
                for (int c = 1; c <= groupCount; c++) {
                    pathValues.add(matcher.group(c));
                }
            }
            List<String> names = routeMapping.getVariableNames();
            for (int i = 0; i < names.size(); i++) {
                params.put(names.get(i), pathValues.get(i));
            }

            if (actionMapping.getParams() == null) {
                actionMapping.setParams(params);
            } else {
                actionMapping.getParams().putAll(params);
            }
        }
    }
}
