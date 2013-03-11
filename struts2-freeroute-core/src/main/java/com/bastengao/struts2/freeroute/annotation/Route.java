package com.bastengao.struts2.freeroute.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置一个路由
 *
 * @author bastengao
 * @date 12-12-16 15:15
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {
    /**
     * 映射的路径
     *
     * @return
     * @since 1.0
     */
    String value() default "";

    /**
     * 映射的 http method, 默认不限制
     *
     * @return
     * @since 1.0
     */
    MethodType[] method() default {};

    /**
     * 指定的参数
     *
     * @return
     * @since 1.0
     */
    String[] params() default {};

    /**
     * 拦截器, 排名区分先后
     *
     * @return
     * @since 1.0.2
     */
    String[] interceptors() default {};
}
