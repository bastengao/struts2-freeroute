package com.bastengao.struts2.freeroute;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * action  result
 *
 * @author bastengao
 * @date 13-1-9 22:22
 */
public class Result {

    public static Result create() {
        return new Result();
    }

    public static Result create(String resultType) {
        return new Result(resultType);
    }

    protected Map<String, String> params = new HashMap<String, String>();
    // 默认类型
    protected String resultType = "dispatcher";

    protected Result() {
    }

    protected Result(String resultType) {
        this.resultType = resultType;
    }

    public Result param(String name, String value) {
        this.params.put(name, value);
        return this;
    }

    public Result location(String value) {
        this.params.put("location", value);
        return this;
    }

    public String done() {
        if(params.isEmpty()){
            return resultType;
        }

        // resultType: params.toJSON()
        JSONObject jsonObject = new JSONObject(params);

        return resultType + ":" + jsonObject.toString();
    }

}
