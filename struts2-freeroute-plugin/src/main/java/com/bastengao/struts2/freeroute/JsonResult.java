package com.bastengao.struts2.freeroute;

/**
 * json result
 *
 * 参考 http://struts.apache.org/2.3.8/docs/json-plugin.html
 *
 * @author bastengao
 * @date 13-1-10 21:47
 * @since 1.0
 */
public class JsonResult extends Result {
    protected JsonResult() {
        this.resultType = "json";
    }

    public JsonResult root(String root) {
        this.param("root", root);
        return this;
    }

    public JsonResult includeProperties(String properties) {
        this.param("includeProperties", properties);
        return this;
    }
}
