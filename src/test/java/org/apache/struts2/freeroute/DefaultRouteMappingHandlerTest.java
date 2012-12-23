package org.apache.struts2.freeroute;

import org.apache.struts2.freeroute.annotation.MethodType;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @author bastengao
 * @date 12-12-22 22:50
 */
public class DefaultRouteMappingHandlerTest {
    @Test
    public void test() {
        Pattern a = Pattern.compile("abc");
        Pattern b = Pattern.compile("abc");

        Assert.assertNotEquals(a, b);
    }

}
