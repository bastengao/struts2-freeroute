package com.bastengao.struts2.freeroute;

/**
 * 生成各种常用返回结果
 *
 * @author bastengao
 * @date 13-1-10 21:19
 * @since 1.0
 */
public class Results {

    /**
     * html result. 返回类型是 "dispatcher"
     * TODO 优化：可自动补全后缀
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String html(String location) {
        return dispatcher(location);
    }

    /**
     * jsp result. 返回类型是 "dispatcher"
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String jsp(String location) {
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
     * freemarker result
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String freemarker(String location) {
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
     * velocity result
     *
     * @param location 页面路径
     * @return
     * @since 1.0
     */
    public static String velocity(String location) {
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
