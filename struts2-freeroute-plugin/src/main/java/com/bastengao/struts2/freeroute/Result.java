package com.bastengao.struts2.freeroute;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * generic action  result
 *
 * @author bastengao
 * @date 13-1-9 22:22
 * @since 1.0
 */
public class Result {

    /**
     * 构造返回结果, resultType 默认为 "dispatcher"
     *
     * @since 1.0
     */
    public static Result create() {
        return new Result();
    }

    /**
     * 构造返回结果
     *
     * @param resultType 返回结果类型
     * @since 1.0
     */
    public static Result create(String resultType) {
        return new Result(resultType);
    }

    // 参数
    protected Map<String, String> params = new HashMap<String, String>();
    // 默认类型
    protected String resultType = "dispatcher";

    /**
     * 构造返回结果, resultType 默认为 "dispatcher"
     *
     * @since 1.0
     */
    protected Result() {
    }

    /**
     * 构造返回结果
     *
     * @param resultType 返回结果类型
     * @since 1.0
     */
    protected Result(String resultType) {
        this.resultType = resultType;
    }

    /**
     * 设置某个参数。可设置多次，如果重名会覆盖之前的值。
     *
     * @param name  参数名称
     * @param value 值
     * @return
     * @since 1.0
     */
    public Result param(String name, String value) {
        this.params.put(name, value);
        return this;
    }

    /**
     * 页面路径，如果有
     *
     * @param value
     * @return
     * @since 1.0
     */
    public Result location(String value) {
        this.params.put("location", value);
        return this;
    }

    /**
     * 完成
     *
     * @return
     * @since 1.0
     */
    public String done() {
        if (params.isEmpty()) {
            return resultType;
        }

        // resultType: params.toJSON()
        JSONObject jsonObject = new JSONObject(params);

        return resultType + ":" + jsonObject.toString();
    }

}
