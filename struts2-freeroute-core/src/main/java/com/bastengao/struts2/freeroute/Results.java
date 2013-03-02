package com.bastengao.struts2.freeroute;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;

/**
 * 生成各种常用返回结果
 *
 * @author bastengao
 * @date 13-1-10 21:19
 * @since 1.0
 */
public class Results {

    /**
     * html result. 返回类型是 "dispatcher", 可自动补全后缀 ".html"
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String html(String location) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(location));
        location = padEnd(location, ".html");

        return dispatcher(location);
    }

    /**
     * jsp result. 返回类型是 "dispatcher", 可自动补全后缀
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String jsp(String location) {
        location = padEnd(location, ".jsp");
        return dispatcher(location);
    }

    /**
     * dispatcher result
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String dispatcher(String location) {
        return "dispatcher:" + location;
    }

    /**
     * freemarker result, alias #freemarker.
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String ftl(String location) {
        return freemarker(location);
    }

    /**
     * freemarker result, 可自动补全后缀
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String freemarker(String location) {
        location = padEnd(location, ".ftl");
        return "freemarker:" + location;
    }

    /**
     * velocity result, alias #velocity
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String vm(String location) {
        return velocity(location);
    }

    /**
     * velocity result, 可自动补全后缀
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String velocity(String location) {
        location = padEnd(location, ".vm");
        return "velocity:" + location;
    }

    /**
     * redirect result
     *
     * @param location 重定向目标
     * @return
     * @since 1.0
     */
    public static String redirect(String location) {
        return result("redirect", location);
    }

    /**
     * 只返回类型. 不需要路径或者参数的，例如 json
     *
     * @param resultType 返回类型
     * @return
     * @since 1.0
     */
    public static String result(String resultType) {
        return resultType;
    }

    /**
     * 快速构造返回结果
     *
     * @param resultType 返回类型
     * @param location   路径
     * @return
     * @since 1.0
     */
    public static String result(String resultType, String location) {
        return resultType + ":" + location;
    }

    /**
     * 自动补全后缀. 如果存在后缀则直接返回，如果不存在则补全
     *
     * @param source
     * @param suffix
     * @return
     */
    @VisibleForTesting
    static String padEnd(String source, String suffix) {
        if (!source.endsWith(suffix)) {
            return source + suffix;
        }

        return source;
    }

    /**
     * json result
     *
     * @return
     * @since 1.0
     */
    public static JsonResult json() {
        return new JsonResult();
    }

    /**
     * chain result
     *
     * @return
     * @since 1.0
     */
    public static ChainResult chain() {
        return new ChainResult();
    }

    /**
     * stream result
     *
     * @return
     * @since 1.0
     */
    public static StreamResult stream() {
        return new StreamResult();
    }

    /**
     * http header result
     *
     * @return
     * @since 1.0
     */
    public static HttpHeaderResult httpHeader() {
        return new HttpHeaderResult();
    }
}
