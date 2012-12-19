package org.apache.struts2.freeroute;

import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author bastengao
 * @date 12-12-16 00:11
 */
public class DefaultActionMapper extends org.apache.struts2.dispatcher.mapper.DefaultActionMapper {
    private static final Logger log = LoggerFactory.getLogger(DefaultActionMapper.class);

    private RouteMappingHandler routeMappingHandler;

    @Inject("routeMappingHandler")
    public void setRouteMappingHandler(RouteMappingHandler routeMappingHandler) {
        log.trace("routeMappingHandler:{}", routeMappingHandler);
        this.routeMappingHandler = routeMappingHandler;
    }

    @Inject(required = false, value = "struts.mapping.myConfig")
    public void setMyConfig(String config) {
        log.debug("config:{}", config);
    }

    @Override
    public ActionMapping getMapping(javax.servlet.http.HttpServletRequest request, ConfigurationManager configManager) {
        log.debug("mapper:{}", this);
        log.debug("getMapping:{}", request);
        log.debug("uri:{}", request.getRequestURI());
        log.debug("url:{}", request.getRequestURL());
        log.debug("servletPath:{}", request.getServletPath());


        ActionMapping actionMapping0 = parseAndFindRouteMapping(request);
        if (actionMapping0 != null) {
            return actionMapping0;
        }


        return super.getMapping(request, configManager);
    }

    private ActionMapping parseAndFindRouteMapping(HttpServletRequest request) {
        RouteMapping routeMapping = routeMappingHandler.route(request);

        if (routeMapping == null) {
            return null;
        }
        log.debug("routeMapping:{}", routeMapping);

        //TODO 这里的的处理步骤与 MyPackageProvider 处理一致
        String routePath = routeMapping.getRoute().value();
        routePath = ActionUtil.padSlash(routePath);
        routePath = RouteUtil.flatRoutePath(routePath);

        String namespace = ActionUtil.namespace(routePath);
        namespace = ActionUtil.padSlash(namespace);
        String actionName = ActionUtil.actionName(routePath);

        ActionMapping actionMapping = new ActionMapping();
        actionMapping.setNamespace(namespace);
        actionMapping.setName(actionName);
        setParams(actionMapping, routeMapping, request);
        return actionMapping;
    }

    private void setParams(ActionMapping actionMapping, RouteMapping routeMapping, HttpServletRequest request) {
        if (routeMapping.hasPathVariables()) {
            Map<String, Object> params = new HashMap<String, Object>();
            String servletPath = request.getServletPath();
            Matcher matcher = routeMapping.getRoutePathPattern().matcher(servletPath);
            List<String> values = new ArrayList<String>();
            //只匹配一次,  完成匹配
            if (matcher.find()) {
                int groupCount = matcher.groupCount();
                for (int c = 1; c <= groupCount; c++) {
                    values.add(matcher.group(c));
                }
            }
            List<String> names = routeMapping.getVariableNames();
            for (int i = 0; i < names.size(); i++) {
                params.put(names.get(i), values.get(i));
            }
            actionMapping.setParams(params);
        }
    }
}
