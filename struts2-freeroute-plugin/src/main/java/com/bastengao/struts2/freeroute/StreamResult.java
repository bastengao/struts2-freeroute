package com.bastengao.struts2.freeroute;

/**
 * stream result
 * 参考: http://struts.apache.org/2.3.8/docs/stream-result.html
 *
 * @author bastengao
 * @date 13-1-10 23:33
 * @since 1.0
 */
public class StreamResult extends Result {
    public StreamResult() {
        super("stream");
    }

    public StreamResult contentType(String value) {
        return this._param("contentType", value);
    }

    public StreamResult contentLength(String value) {
        return this._param("contentLength", value);
    }

    public StreamResult contentDisposition(String value) {
        return this._param("contentDisposition", value);
    }

    public StreamResult inputName(String value) {
        return this._param("inputName", value);
    }

    public StreamResult bufferSize(String value) {
        return this._param("bufferSize", value);
    }

    public StreamResult allowCaching(String value) {
        return this._param("allowCaching", value);
    }

    public StreamResult contentCharSet(String value) {
        return this._param("contentCharSet", value);
    }

    private StreamResult _param(String name, String value) {
        super.param(name, value);
        return this;
    }
}
