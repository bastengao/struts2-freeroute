package com.bastengao.struts2.freeroute.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内容路径
 *
 * @author bastengao
 * @date 12-12-16 15:24
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentBase {
    /**
     * 内容基路径
     *
     * @return
     * @since 1.0
     */
    String value() default "";
}
