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
    public static final Map<String, String> SUFFIXES = new HashMap<String, String>();

    static {
        // TODO 鸡肋，考虑去去掉后缀补全. 要么都不要后缀要么都加后缀
        SUFFIXES.put("freemarker", ".ftl");
        SUFFIXES.put("velocity", ".vm");
        SUFFIXES.put("dispatcher", "");
        SUFFIXES.put("json", "");
        SUFFIXES.put("redirect", "");
    }

    private ObjectFactory objectFactory;
    private Configuration configuration;

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
        if (resultConfig == null) {
            return null;
        }

        return resultConfig;
    }

    /**
     * 找能够处理的 resultType, 目前只支持 dispatcher, freemarker, velocity
     *
     * @param resultCode
     * @param resultTypes
     * @return
     */
    private ResultConfig findResultConfig(String resultCode, Map<String, ResultTypeConfig> resultTypes) {
        // 动态处理内容的路径，目前只支持 velocity, freemarker, jsp, html
        for (String type : SUFFIXES.keySet()) {
            //startWith("type:")
            if (resultCode.startsWith(type + ":")) {
                ResultTypeConfig typeConfig = resultTypes.get(type);
                ResultConfig.Builder resultBuilder = new ResultConfig.Builder(resultCode, typeConfig.getClassName());
                if (typeConfig.getParams() != null) {
                    resultBuilder.addParams(typeConfig.getParams());
                }

                String path = resultCode.substring(type.length() + 1);
                String suffix = SUFFIXES.get(type); //后缀
                if (!Strings.isNullOrEmpty(typeConfig.getDefaultResultParam())) {
                    resultBuilder.addParam(typeConfig.getDefaultResultParam(), path + suffix);
                }
                return resultBuilder.build();
            }
        }

        return null;
    }

}
