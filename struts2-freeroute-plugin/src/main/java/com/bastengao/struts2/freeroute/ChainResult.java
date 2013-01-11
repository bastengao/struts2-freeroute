package com.bastengao.struts2.freeroute;

/**
 * chain result
 * 参考: http://struts.apache.org/2.3.8/docs/chain-result.html
 *
 * @author bastengao
 * @date 13-1-10 23:32
 * @since 1.0
 */
public class ChainResult extends Result {

    public ChainResult() {
        super("chain");
    }

    public ChainResult actionName(String value) {
        return this._param("actionName", value);
    }

    public ChainResult namespace(String value) {
        return this._param("namespace", value);
    }

    public ChainResult method(String value) {
        return this._param("method", value);
    }

    public ChainResult skipActions(String value) {
        return this._param("skipActions", value);
    }

    private ChainResult _param(String name, String value) {
        super.param(name, value);
        return this;
    }
}
