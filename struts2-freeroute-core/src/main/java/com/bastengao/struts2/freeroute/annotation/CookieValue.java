package com.bastengao.struts2.freeroute.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定 cookie
 *
 * @author bastengao
 * @date 13-1-7 21:08
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CookieValue {
    /**
     * 要绑定的 cookie name
     *
     * @return
     * @since 1.0
     */
    String value();
}
