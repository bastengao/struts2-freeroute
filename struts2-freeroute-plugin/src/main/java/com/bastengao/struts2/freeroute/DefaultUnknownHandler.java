package com.bastengao.struts2.freeroute;

import com.google.common.base.Strings;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.UnknownHandler;
import com.opensymphony.xwork2.XWorkException;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.config.entities.ResultTypeConfig;
import com.opensymphony.xwork2.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
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
    public com.opensymphony.xwork2.Result handleUnknownResult(ActionContext actionContext, String actionName, ActionConfig actionConfig, String resultCode) throws XWorkException {
        //这是我的菜, 更擅长处理未知的 result
        log.debug("catch result[{}] of action[{}]", resultCode, actionName);
        RouteMapping routeMapping = routeMappingHandler.route(actionConfig);
        log.debug("route: {}" + routeMapping);

        ResultConfig resultConfig = parseResultCodeToResultConfig(actionConfig, resultCode, routeMapping);
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
     * @param routeMapping
     * @return
     */
    private ResultConfig parseResultCodeToResultConfig(ActionConfig actionConfig, String resultCode, RouteMapping routeMapping) {
        String packageName = actionConfig.getPackageName();
        PackageConfig packageConfig = configuration.getPackageConfig(packageName);
        Map<String, ResultTypeConfig> resultTypes = packageConfig.getAllResultTypeConfigs();

        log.debug("packageName:{}", packageName);
        log.debug("resultTypes:{}", resultTypes);

        return findResultConfig(routeMapping, resultCode, resultTypes);
    }

    /**
     * 找能够处理的 resultType, 目前只支持 dispatcher, freemarker, velocity, json, redirect
     *
     * @param routeMapping
     * @param resultCode
     * @param resultTypes
     * @return
     */
    private ResultConfig findResultConfig(RouteMapping routeMapping, String resultCode, Map<String, ResultTypeConfig> resultTypes) {
        // 动态处理内容的路径，目前只支持 velocity, freemarker, jsp, html, json, redirect
        // TODO 去掉类型限制
        for (String type : AVAILABLE_TYPES.keySet()) {
            //如果是某种返回类型开始,如 "json" 或者 "dispatcher:/content.html"
            if (!resultCode.startsWith(type)) {
                continue; //如果不是，则下一个
            }

            ResultTypeConfig typeConfig = resultTypes.get(type);
            //如果没有默认参数
            if (Strings.isNullOrEmpty(typeConfig.getDefaultResultParam())) {
                ResultConfig.Builder resultBuilder = createResultConfigFromResultType(resultCode, typeConfig);
                // resultValue 有两种可能: 一种直接是默认参数(比如路径 dispatcher:/xxx.html), 另一种是 json 参数
                // 只有类型, 例如是 "json"
                if (resultCode.length() == type.length()) {
                    return resultBuilder.build();
                }
                if (resultCode.indexOf(":") == type.length()) {
                    String resultParam = resultCode.substring(type.length() + 1);
                    addParamByJSON(resultBuilder, resultParam);
                    return resultBuilder.build();
                }
            }
            //如果有默认参数
            else {
                //  如果是 "type:" 这种形式
                if (resultCode.indexOf(":") == type.length()) {
                    ResultConfig.Builder resultBuilder = createResultConfigFromResultType(resultCode, typeConfig);

                    // resultValue 有两种可能:
                    // 1.一种直接是默认参数(比如路径 dispatcher:/xxx.html)
                    // 2.另一种是 json 参数
                    String resultParam = resultCode.substring(type.length() + 1);
                    if (isJSONObject(resultParam)) {
                        addParamByJSON(resultBuilder, resultParam);
                        return resultBuilder.build();

                    } else {
                        String path = resultParam;
                        path = parsePath(routeMapping, path);
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


    /**
     * 解析路径
     * 区分相对路径还是绝对路径。如果是相对路径那么前追加 @ContentBase, 如果是绝对路径则不需要.
     *
     * @param routeMapping
     * @param originPath
     * @return
     */
    private static String parsePath(RouteMapping routeMapping, String originPath) {
        //如果是绝对路径
        if (originPath.startsWith("/")) {
            // 啥也不干
            return originPath;
        }
        //如果是相对路径
        else {
            //如果有 @ContentBase 配置，在 path 前追加 @ContentBase. 如果没有则将路径转换为绝对路径进行尝试
            if (routeMapping.getContentBase() == null) {
                originPath = ActionUtil.padSlash(originPath);
            } else {
                originPath = ActionUtil.padSlash(routeMapping.getContentBase().value()) + ActionUtil.padSlash(originPath);
            }
        }

        return originPath;
    }

    /**
     * 测试是否是 json 参数
     *
     * @param param
     * @return
     */
    private static boolean isJSONObject(String param) {
        try {
            new JSONObject(param);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private static void addParamByJSON(ResultConfig.Builder resultBuilder, String resultParam) {
        try {
            JSONObject jsonObject = new JSONObject(resultParam);
            for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                resultBuilder.addParam(key, jsonObject.getString(key));
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException("could not parse result param", e);
        }
    }
}
