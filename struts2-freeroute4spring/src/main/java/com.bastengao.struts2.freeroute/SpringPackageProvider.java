package com.bastengao.struts2.freeroute;

import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.spring.StrutsSpringObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * struts2-spring-plugin 插件存在的情况下
 *
 * @author bastengao
 * @date 13-1-13 16:20
 */
public class SpringPackageProvider extends ControllerPackageProvider {
    private static final Logger log = LoggerFactory.getLogger(SpringPackageProvider.class);

    private ServletContext servletContext;
    private ObjectFactory objectFactory;
    private WebApplicationContext appContext;

    @Inject
    public SpringPackageProvider(@Inject ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Inject(value = "spring", required = true)
    private void setStrutsSpringObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
        if (objectFactory instanceof StrutsSpringObjectFactory) {
            WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            this.appContext = appContext;
        }
    }


    @Override
    protected ActionConfig createActionConfig(PackageConfig.Builder packageConfigBuilder, String className, String methodName, String actionName) {
        try {
            // 将 action class name 转换为 spring bean name, 如果可以转
            Class actionClass = objectFactory.getClassInstance(className);
            String[] beanNames = appContext.getBeanNamesForType(actionClass);
            // TODO 一个 action 如果有多个 bean name 暂时没有考虑
            // 目前使用最后了个 bean name
            for (String beanName : beanNames) {
                className = beanName;
                log.trace("bean name: {}", beanName);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        return super.createActionConfig(packageConfigBuilder, className, methodName, actionName);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
