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

    @Test
    public void testPadEnd() {
        String result = Results.padEnd("test.html", ".html");
        Assert.assertEquals("test.html", result);

        result = Results.padEnd("test", ".html");
        Assert.assertEquals("test.html", result);
    }

    @Test
    public void testAutoCompleteSuffix() {
        Assert.assertEquals("dispatcher:/test.html", Results.html("/test.html"));
        Assert.assertEquals("dispatcher:/test.html", Results.html("/test"));

        Assert.assertEquals("dispatcher:/test.jsp", Results.jsp("/test.jsp"));
        Assert.assertEquals("dispatcher:/test.jsp", Results.jsp("/test"));

        Assert.assertEquals("freemarker:/test.ftl", Results.ftl("/test.ftl"));
        Assert.assertEquals("freemarker:/test.ftl", Results.freemarker("/test.ftl"));

        Assert.assertEquals("freemarker:/test.ftl", Results.ftl("/test"));
        Assert.assertEquals("freemarker:/test.ftl", Results.freemarker("/test"));

        Assert.assertEquals("velocity:/test.vm", Results.vm("/test.vm"));
        Assert.assertEquals("velocity:/test.vm", Results.velocity("/test.vm"));

        Assert.assertEquals("velocity:/test.vm", Results.vm("/test"));
        Assert.assertEquals("velocity:/test.vm", Results.velocity("/test"));
    }

    @Test
    public void testRedirect(){
        String result = Results.redirect("/page.html");
        Assert.assertEquals("redirect:/page.html", result);
    }

    @Test
    public void testCustomResult() {
        Assert.assertNotNull(Results.json());

        Assert.assertNotNull(Results.chain());

        Assert.assertNotNull(Results.stream());

        Assert.assertNotNull(Results.httpHeader());
    }
}
