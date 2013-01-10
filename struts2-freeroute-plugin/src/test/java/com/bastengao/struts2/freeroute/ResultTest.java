package com.bastengao.struts2.freeroute;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
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
        Assert.assertNotNull(jsonObject);

        JSONObject jsonObject2 = new JSONObject("{name:value, name2: value2}");
        Assert.assertNotNull(jsonObject2);

        JSONObject jsonObject3 = new JSONObject("{name:value, name2: \"${value2}\"}");
        System.out.println(jsonObject3);
        Assert.assertNotNull(jsonObject3);
    }

    @Test(expected = JSONException.class)
    public void testReadJson2() throws JSONException {
        new JSONObject("abc");
    }
}
