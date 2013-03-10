package com.bastengao.struts2.freeroute.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 包配置
 *
 * @author bastengao
 * @date 13-3-10 下午1:09
 * @since 1.0.2
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerPackage {
    /**
     * @return 父包名称
     * @since 1.0.2
     */
    public String parent();
}
