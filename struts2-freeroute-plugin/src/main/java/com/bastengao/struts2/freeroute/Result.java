package com.bastengao.struts2.freeroute;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 产生 action  result
 *
 * @author bastengao
 * @date 13-1-9 22:22
 */
public class Result {
    public static String html(String location) {
        return dispatcher(location);
    }

    public static String jsp(String location) {
        return dispatcher(location);
    }

    public static String dispatcher(String location) {
        return "dispatcher:" + location;
    }

    public static String ftl(String location) {
        return freemarker(location);
    }

    public static String freemarker(String location) {
        return "freemarker:" + location;
    }

    /**
     * alias #velocity
     * @param location
     * @return
     */
    public static String vm(String location) {
        return velocity(location);
    }

    public static String velocity(String location) {
        return "velocity:" + location;
    }

    public static String redirect(String location) {
        return result("redirect", location);
    }

    public static String result(String resultType) {
        return resultType;
    }

    public static String result(String resultType, String location) {
        return resultType + ":" + location;
    }

    public static Result create() {
        return new Result();
    }

    public static Result create(String resultType) {
        return new Result(resultType);
    }

    private Map<String, String> params = new HashMap<String, String>();
    private String resultType = "dispatcher";

    private Result() {
    }

    private Result(String resultType) {
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
        // resultType: params.toJSON()
        JSONObject jsonObject = new JSONObject(params);

        return resultType + ":" + jsonObject.toString();
    }

}
