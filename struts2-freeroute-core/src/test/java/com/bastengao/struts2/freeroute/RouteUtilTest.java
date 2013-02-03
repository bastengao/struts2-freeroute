package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.MethodType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author bastengao
 * @date 12-12-18 23:29
 */
public class RouteUtilTest {

    @Test
    public void testHashPathVariables() {
        Assert.assertTrue(RouteUtil.hasPathVariables("/p/{id}"));
        Assert.assertTrue(RouteUtil.hasPathVariables("/p/{id}.xml"));

        Assert.assertFalse(RouteUtil.hasPathVariables("/p/123{id}"));
        Assert.assertFalse(RouteUtil.hasPathVariables("/p/id"));
    }

    @Test
    public void test() {
        String routePath = "/persons/";
        Assert.assertEquals("/persons/", RouteUtil.flatRoutePath(routePath));

        routePath = "/persons/{";
        Assert.assertEquals("/persons/{", RouteUtil.flatRoutePath(routePath));

        routePath = "/persons/{id}";
        Assert.assertEquals("/persons/__id__", RouteUtil.flatRoutePath(routePath));

        routePath = "/persons/{id}/edit";
        Assert.assertEquals("/persons/__id__/edit", RouteUtil.flatRoutePath(routePath));

        routePath = "/persons/{id}/edit/{name}";
        Assert.assertEquals("/persons/__id__/edit/__name__", RouteUtil.flatRoutePath(routePath));
    }

    @Test
    public void testPathVariablePattern() {
        // "{[a-zA-Z]+}"
        //"/{[a-zA-Z]+}"
        // "^([?:/|\w]*/{[(a-zA-Z)]+})+$"
        //Pattern pattern = Pattern.compile("^([/|\\w]*/\\{[a-zA-Z]+\\})+$");
        Pattern pattern = Pattern.compile("/\\{([a-zA-Z]+)\\}");

        String path = "/{id}";
        Assert.assertTrue(pattern.matcher("/{id}").find());
        Assert.assertTrue(pattern.matcher("/persons/{id}").find());
        Assert.assertTrue(pattern.matcher("/persons/{id}.xml").find());
        Assert.assertTrue(pattern.matcher("/persons/{id}/edit").find());

        Assert.assertTrue(pattern.matcher("/persons/{id}/edit/{name}").find());

        Assert.assertFalse(pattern.matcher("{id}").find());
        Assert.assertFalse(pattern.matcher("/persons/{}").find());
    }

    @Test
    public void testToRoutePathPattern() {
        String patternStr = RouteUtil.toRoutePathPattern("/persons/{id}");
        Assert.assertTrue(Pattern.compile(patternStr).matcher("/persons/123").matches());

        String patternStr2 = RouteUtil.toRoutePathPattern("/persons/{id}/edit/{name}");
        Pattern pattern2 = Pattern.compile(patternStr2);
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten").matches());

        Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten.gao").matches());
        //Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten<gao").matches());
        //Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten>gao").matches());
        //Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten^gao").matches());
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten-gao").matches());
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten_gao").matches());
        //Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten`gao").matches());
        //Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten}gao").matches());
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten~gao").matches());
        //Assert.assertTrue(pattern2.matcher("/persons/123/edit/basten\"gao").matches());
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/中文").matches());
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/中.文").matches());
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/中-文").matches());
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/中_文").matches());
        Assert.assertTrue(pattern2.matcher("/persons/123/edit/中~文").matches());

        Assert.assertFalse(pattern2.matcher("/persons/123/edit/中文?").matches());
        Assert.assertFalse(pattern2.matcher("/persons/123/edit/中文#").matches());
        Assert.assertFalse(pattern2.matcher("/persons/123/edit/中文;").matches());
        Assert.assertFalse(pattern2.matcher("/persons/123/edit/中文,").matches());
    }

    @Test
    public void testVariableNames() {
        List<String> names = RouteUtil.pathVariableNames("/persons/{id}");
        Assert.assertEquals(1, names.size());
        Assert.assertEquals("id", names.get(0));

        List<String> names2 = RouteUtil.pathVariableNames("/persons/{id}/edit/{name}");
        Assert.assertEquals(2, names2.size());
        Assert.assertEquals("id", names2.get(0));
        Assert.assertEquals("name", names2.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVariableNames2() {
        RouteUtil.pathVariableNames("/persons/{id}/id/{id}");
    }

    @Test
    public void testValueOf() {
        Assert.assertSame(MethodType.GET, RouteUtil.valueOfMethod("GET"));

        Assert.assertSame(MethodType.GET, RouteUtil.valueOfMethod("get"));

        Assert.assertNull(RouteUtil.valueOfMethod("notExists"));
    }
}
