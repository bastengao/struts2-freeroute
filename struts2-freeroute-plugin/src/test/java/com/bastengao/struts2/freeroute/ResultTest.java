package com.bastengao.struts2.freeroute;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * @author bastengao
 * @date 13-1-9 22:41
 */
public class ResultTest {
    @Test
    public void testResult() {
        String value = Result.create().param("location", "value.jsp").done();
        System.out.println(value);

        value = Result.create("freemarker").location("value.jsp").done();
        System.out.println(value);

        value = Result.create("chain").param("name1", "value1").param("name2", "value2").done();
        System.out.println(value);
    }

    @Test
    public void testReadJson() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"name\":\"value\"}");
        System.out.println(jsonObject);
    }
}
