package com.bastengao.struts2.freeroute;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bastengao
 * @date 13-1-14 23:02
 */
public class DefaultUnknownHandlerTest {

    @Test
    public void testParseResultType() {
        String resultType = DefaultUnknownHandler.parseResultType("dispatcher");
        Assert.assertEquals("dispatcher", resultType);

        resultType = DefaultUnknownHandler.parseResultType("dispatcher:abc.html");
        Assert.assertEquals("dispatcher", resultType);
    }
}
