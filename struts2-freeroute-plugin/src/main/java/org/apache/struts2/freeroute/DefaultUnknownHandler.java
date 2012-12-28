package org.apache.struts2.freeroute;

import com.google.common.base.Strings;
import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.config.entities.ResultTypeConfig;
import com.opensymphony.xwork2.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认处理未知的 result 或者 action.
 * <p/>
 * 实现此类，主要通过获取 action 动态返回的 result 映射到对应的页面
 *
 * @author bastengao
 * @date 12-12-18 22:03
 */
public class DefaultUnknownHandler implements UnknownHandler {
    private final static Logger log = LoggerFactory.getLogger(DefaultUnknownHandler.class);
    //可用的返回类型
    public static final Map<String, String> AVAILABLE_TYPES = new HashMap<String, String>();

    static {
        AVAILABLE_TYPES.put("freemarker", "");
        AVAILABLE_TYPES.put("velocity", "");
        AVAILABLE_TYPES.put("dispatcher", "");
        AVAILABLE_TYPES.put("json", "");
        AVAILABLE_TYPES.put("redirect", "");
    }

    private ObjectFactory objectFactory;
    private Configuration configuration;
    private RouteMappingHandler routeMappingHandler;

    @Inject("routeMappingHandler")
    private void setRouteMappingHandler(RouteMappingHandler routeMappingHandler) {
        this.routeMappingHandler = routeMappingHandler;
    }

    @Inject
    public DefaultUnknownHandler(ObjectFactory objectFactory, Configuration configuration) {
        this.objectFactory = objectFactory;
        this.configuration = configuration;
    }

    @Override
    public ActionConfig handleUnknownAction(String namespace, String actionName) throws XWorkException {
        //这不是我的菜
        return null;
    }

    @Override
    public Result handleUnknownResult(ActionContext actionContext, String actionName, ActionConfig actionConfig, String resultCode) throws XWorkException {
        //这是我的菜, 更擅长处理未知的 result
        log.debug("catch result[{}] of action[{}]", resultCode, actionName);
        RouteMapping routeMapping = routeMappingHandler.route(actionConfig);
        log.debug("route: {}" + routeMapping);

        ResultConfig resultConfig = parseResultCodeToResultConfig(actionConfig, resultCode);
        if (resultConfig == null) {
            return null;
        }

        try {
            return objectFactory.buildResult(resultConfig, actionContext.getContextMap());
        } catch (Exception e) {
            throw new XWorkException("Unable to build result", e, resultConfig);
        }
    }

    @Override
    public Object handleUnknownActionMethod(Object action, String methodName) throws NoSuchMethodException {
        //这不是我的菜
        return null;
    }

    /**
     * 解析 resultCode 为 ResultConfig, 如果无法解析则返回 null
     *
     * @param actionConfig
     * @param resultCode
     * @return
     */
    private ResultConfig parseResultCodeToResultConfig(ActionConfig actionConfig, String resultCode) {
        String packageName = actionConfig.getPackageName();
        PackageConfig packageConfig = configuration.getPackageConfig(packageName);
        Map<String, ResultTypeConfig> resultTypes = packageConfig.getAllResultTypeConfigs();

        log.debug("packageName:{}", packageName);
        log.debug("resultTypes:{}", resultTypes);

        ResultConfig resultConfig = findResultConfig(resultCode, resultTypes);
        return resultConfig;
    }

    /**
     * 找能够处理的 resultType, 目前只支持 dispatcher, freemarker, velocity, json, redirect
     *
     * @param resultCode
     * @param resultTypes
     * @return
     */
    private ResultConfig findResultConfig(String resultCode, Map<String, ResultTypeConfig> resultTypes) {
        // 动态处理内容的路径，目前只支持 velocity, freemarker, jsp, html, json, redirect
        for (String type : AVAILABLE_TYPES.keySet()) {
            //如果是某种返回类型开始,如 "json" 或者 "dispatcher:/content.html"
            if (resultCode.startsWith(type)) {
                ResultTypeConfig typeConfig = resultTypes.get(type);
                //如果没有默认参数
                if (Strings.isNullOrEmpty(typeConfig.getDefaultResultParam())) {
                    // 只有类型, 例如是 "json" 或者 "json:" 而不是 "jsonXxx"
                    if (resultCode.length() == type.length() || resultCode.indexOf(":") == type.length()) {
                        ResultConfig.Builder resultBuilder = createResultConfigFromResultType(resultCode, typeConfig);
                        return resultBuilder.build();
                    }
                }
                //如果有默认参数
                else {
                    //  如果是 "type:" 这种形式
                    if (resultCode.indexOf(":") == type.length()) {
                        ResultConfig.Builder resultBuilder = createResultConfigFromResultType(resultCode, typeConfig);

                        String path = resultCode.substring(type.length() + 1);
                        resultBuilder.addParam(typeConfig.getDefaultResultParam(), path);
                        return resultBuilder.build();
                    }
                }
            }
        }

        return null;
    }

    /**
     * 通过 ResultTypeConfig 构造 ResultConfig.Builder
     *
     * @param resultCode
     * @param typeConfig
     * @return
     */
    private static ResultConfig.Builder createResultConfigFromResultType(String resultCode, ResultTypeConfig typeConfig) {
        ResultConfig.Builder resultBuilder = new ResultConfig.Builder(resultCode, typeConfig.getClassName());
        if (typeConfig.getParams() != null) {
            resultBuilder.addParams(typeConfig.getParams());
        }

        return resultBuilder;
    }

}
