package com.bastengao.struts2.freeroute;

import com.opensymphony.xwork2.ActionContext;

/**
 * json result of JSON plugin.
 * 参考 http://struts.apache.org/2.3.8/docs/json-plugin.html
 *
 * @author bastengao
 * @date 13-1-10 21:47
 * @since 1.0
 */
public class JsonResult extends Result {
    protected JsonResult() {
        super("json");
    }

    /**
     * 将 data 做为 root
     *
     * @param data
     * @return
     */
    public JsonResult asRoot(Object data){
        ActionContext.getContext().put("_jsonRootData", data);
        return root("_jsonRootData");
    }

    public JsonResult root(String root) {
        return this._param("root", root);
    }

    public JsonResult includeProperties(String properties) {
        return this._param("includeProperties", properties);
    }

    public JsonResult excludeProperties(String properties) {
        return this._param("excludeProperties", properties);
    }

    public JsonResult wrapPrefix(String value) {
        return this._param("wrapPrefix", value);
    }

    public JsonResult wrapSuffix(String value) {
        return this._param("wrapSuffix", value);
    }

    public JsonResult prefix(String value) {
        return this._param("prefix", value);
    }

    public JsonResult ignoreHierarchy(boolean value) {
        return this._param("ignoreHierarchy", String.valueOf(value));
    }

    public JsonResult enumAsBean(boolean value) {
        return this._param("enumAsBean", String.valueOf(value));
    }

    public JsonResult enableGZIP(boolean value) {
        return this._param("enableGZIP", String.valueOf(value));
    }

    public JsonResult noCache(boolean value) {
        return this._param("noCache", String.valueOf(value));
    }

    public JsonResult excludeNullProperties(boolean value) {
        return this._param("excludeNullProperties", String.valueOf(value));
    }

    public JsonResult statusCode(String value) {
        return this._param("statusCode", value);
    }

    public JsonResult errorCode(String value) {
        return this._param("errorCode", value);
    }

    public JsonResult callbackParameter(String value) {
        return this._param("callbackParameter", value);
    }

    public JsonResult contentType(String value) {
        return this._param("contentType", value);
    }

    public JsonResult encoding(String value) {
        return this._param("encoding", value);
    }

    private JsonResult _param(String name, String value) {
        super.param(name, value);
        return this;
    }
}
