package com.bastengao.struts2.freeroute.annotation;

/**
 * enum of http method, 与 Servlet API method 名称一致, 除了 NONE
 *
 * @author bastengao
 * @date 12-12-16 15:31
 * @since 1.0
 */
public enum MethodType {
    DELETE,
    GET,
    HEAD,
    OPTIONS,
    POST,
    PUT,
    TRACE,

    /**
     * any method
     * @deprecated Route method 默认为空数组，不需要此枚举了
     */
    NONE;
}
