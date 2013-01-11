package com.bastengao.struts2.freeroute;

/**
 * @author bastengao
 * @date 13-1-10 21:19
 * @since 1.0
 */
public class Results {

    /**
     * TODO 优化：可自动补全后缀
     *
     * @param location
     * @return
     */
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
     *
     * @param location
     * @return
     * @since 1.0
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
