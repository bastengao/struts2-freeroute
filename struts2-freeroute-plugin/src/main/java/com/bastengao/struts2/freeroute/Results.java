package com.bastengao.struts2.freeroute;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 产生 action  result
 *
 * @author bastengao
 * @date 13-1-9 22:22
 */
public class Results {
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

    public static Results create() {
        return new Results();
    }

    public static Results create(String resultType) {
        return new Results(resultType);
    }

    private Map<String, String> params = new HashMap<String, String>();
    private String resultType = "dispatcher";

    private Results() {
    }

    private Results(String resultType) {
        this.resultType = resultType;
    }

    public Results param(String name, String value) {
        this.params.put(name, value);
        return this;
    }

    public Results location(String value) {
        this.params.put("location", value);
        return this;
    }

    public String done() {
        // resultType: params.toJSON()
        JSONObject jsonObject = new JSONObject(params);

        return resultType + ":" + jsonObject.toString();
    }

}
