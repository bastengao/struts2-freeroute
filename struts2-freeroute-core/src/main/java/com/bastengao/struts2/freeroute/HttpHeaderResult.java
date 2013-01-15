package com.bastengao.struts2.freeroute;

/**
 * http header result.
 * 参考: http://struts.apache.org/2.3.8/docs/httpheader-result.html
 *
 * @author bastengao
 * @date 13-1-10 23:34
 * @since 1.0
 */
public class HttpHeaderResult extends Result {
    public HttpHeaderResult() {
        super("httpheader");
    }

    public HttpHeaderResult status(String value) {
        return this._param("status", value);
    }

    public HttpHeaderResult parse(boolean value) {
        return this._param("parse", String.valueOf(value));
    }

    public HttpHeaderResult headers(String name, String value) {
        return this._param("headers." + name, value);
    }

    public HttpHeaderResult error(String value) {
        return this._param("error", value);
    }

    public HttpHeaderResult errorMessage(String value) {
        return this._param("errorMessage", value);
    }

    private HttpHeaderResult _param(String name, String value) {
        super.param(name, value);
        return this;
    }
}

