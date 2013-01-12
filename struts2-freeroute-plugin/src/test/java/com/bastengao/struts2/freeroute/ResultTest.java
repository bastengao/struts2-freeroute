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
        Assert.assertNotNull(value);

        value = Result.create("freemarker").location("value.jsp").done();
        Assert.assertNotNull(value);

        value = Result.create("chain").param("name1", "value1").param("name2", "value2").done();
        Assert.assertNotNull(value);
    }

    @Test
    public void testResult2() {
        String result = Result.create().location("/abc.html").done();
        Assert.assertNotNull(result);
    }

    @Test
    public void testReadJson() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"name\":\"value\"}");
        Assert.assertNotNull(jsonObject);
        Assert.assertEquals("value", jsonObject.getString("name"));

        JSONObject jsonObject2 = new JSONObject("{name:value, name2: value2}");
        Assert.assertNotNull(jsonObject2);
        Assert.assertEquals("value", jsonObject2.getString("name"));

        JSONObject jsonObject3 = new JSONObject("{name:value, name2: \"${value2}\"}");
        Assert.assertNotNull(jsonObject3);
        Assert.assertEquals("${value2}", jsonObject3.getString("name2"));
    }

    @Test(expected = JSONException.class)
    public void testReadJson2() throws JSONException {
        new JSONObject("abc");
    }
}
