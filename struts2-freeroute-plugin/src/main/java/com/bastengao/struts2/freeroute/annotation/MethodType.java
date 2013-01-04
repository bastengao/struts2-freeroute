package com.bastengao.struts2.freeroute.annotation;

/**
 * enum of http method, 与 Servlet API method 名称一致, 除了 NONE 之外
 *
 * @author bastengao
 * @date 12-12-16 15:31
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
     */
    NONE;
}
