package com.bastengao.struts2.freeroute;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bastengao
 * @date 13-1-12 16:45
 */
public class ResultsTest {
    @Test
    public void testOnlyType() {
        String result = Results.result("json");
        Assert.assertNotNull(result);
        Assert.assertEquals("json", result);
    }

    @Test
    public void testResult() {
        String result = Results.result("dispatcher", "/test.html");
        Assert.assertNotNull(result);
        Assert.assertEquals("dispatcher:/test.html", result);
    }

    @Test
    public void testLocationResult() {
        String result = Results.dispatcher("/test.html");
        Assert.assertNotNull(result);
        Assert.assertEquals("dispatcher:/test.html", result);

        String result2 = Results.html("/test.html");
        Assert.assertNotNull(result2);
        Assert.assertEquals("dispatcher:/test.html", result2);

        String result3 = Results.jsp("/test.jsp");
        Assert.assertNotNull(result3);
        Assert.assertEquals("dispatcher:/test.jsp", result3);
    }
}
