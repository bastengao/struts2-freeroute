package com.bastengao.struts2.freeroute;

/**
 * json result of JSON plugin
 * <p/>
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

    public JsonResult ignoreHierarchy(String value) {
        //TODO 将 value 改为 boolean 类型
        return this._param("ignoreHierarchy", value);
    }

    public JsonResult enumAsBean(String value) {
        //TODO 将 value 改为 boolean 类型
        return this._param("enumAsBean", value);
    }

    public JsonResult enableGZIP(String value) {
        return this._param("enableGZIP", value);
    }

    public JsonResult noCache(String value) {
        return this._param("noCache", value);
    }

    public JsonResult excludeNullProperties(String value) {
        return this._param("excludeNullProperties", value);
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
