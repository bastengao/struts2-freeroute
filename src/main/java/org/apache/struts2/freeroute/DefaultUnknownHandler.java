package org.apache.struts2.freeroute;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private ObjectFactory objectFactory;

    @Inject
    public DefaultUnknownHandler(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
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

        //TODO 动态处理内容的路径，形式如 velocity, freemarker, jsp, html
        String resultPath = ActionUtil.padSlash(resultCode + ".html");
        log.debug("resultPath:{}", resultPath);
        ResultConfig.Builder resultBuilder = new ResultConfig.Builder(resultCode, "org.apache.struts2.dispatcher.ServletDispatcherResult");
        resultBuilder.addParam("location", resultPath);
        ResultConfig resultConfig = resultBuilder.build();


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
}
