package com.bastengao.struts2.freeroute;

import com.google.common.collect.Sets;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author bastengao
 * @date 13-2-2 20:09
 */
public class ParamTest {
    private HttpServletRequest request;

    @Before
    public void before() {
        request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter("foo")).andReturn("bar");
        EasyMock.replay(request);
    }

    @Test
    public void testExists() {
        RouteMapping.Param param = new RouteMapping.Param("foo");
        Set<String> paramNames = Sets.newHashSet("foo");


        boolean result = param.match(request, paramNames);
        Assert.assertTrue(result);
    }

    @Test
    public void testExists2() {
        RouteMapping.Param param = new RouteMapping.Param("not-exist");
        Set<String> paramNames = Sets.newHashSet("foo");

        boolean result = param.match(request, paramNames);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual() {
        RouteMapping.Param param = new RouteMapping.Param("foo=bar");
        Set<String> paramNames = Sets.newHashSet("foo");

        boolean result = param.match(request, paramNames);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual2() {
        RouteMapping.Param param = new RouteMapping.Param("foo=bbaarr");
        Set<String> paramNames = Sets.newHashSet("foo");

        boolean result = param.match(request, paramNames);
        Assert.assertFalse(result);
    }

    @Test
    public void testNotEqual() {
        RouteMapping.Param param = new RouteMapping.Param("foo!=bbaarr");
        Set<String> paramNames = Sets.newHashSet("foo");

        boolean result = param.match(request, paramNames);
        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqual2() {
        RouteMapping.Param param = new RouteMapping.Param("foo!=bar");
        Set<String> paramNames = Sets.newHashSet("foo");

        boolean result = param.match(request, paramNames);
        Assert.assertFalse(result);
    }
}
