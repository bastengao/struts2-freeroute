package org.apache.struts2.freeroute;

import org.apache.struts2.freeroute.annotation.MethodType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
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
        String routePath = "/persons/{id}";
        Assert.assertEquals("/persons/__id__", RouteUtil.flatRoutePath(routePath));

        routePath = "/persons/{id}/edit";
        Assert.assertEquals("/persons/__id__/edit", RouteUtil.flatRoutePath(routePath));
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
        groups(pattern.matcher("/persons/{id}/edit/{name}"));

        Assert.assertFalse(pattern.matcher("{id}").find());
        Assert.assertFalse(pattern.matcher("/persons/{}").find());
    }

    private void groups(Matcher matcher) {
        while (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group());
        }
    }

    @Test
    public void testToRoutePathPattern() {
        String patternStr = RouteUtil.toRoutePathPattern("/persons/{id}");
        Assert.assertEquals("/persons/([a-zA-Z0-9]+)", patternStr);
    }

    @Test
    public void testToRoutePathPattern2() {
        String patternStr = RouteUtil.toRoutePathPattern("/persons/{id}");
        Matcher matcher = Pattern.compile(patternStr).matcher("/persons/123");
        Assert.assertTrue(matcher.matches());

        String patternStr2 = RouteUtil.toRoutePathPattern("/persons/{id}/edit/{name}");
        Matcher matcher2 = Pattern.compile(patternStr2).matcher("/persons/123/edit/basten");
        Assert.assertTrue(matcher2.matches());
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

    @Test
    public void testValueOf() {
        Assert.assertSame(MethodType.GET, RouteUtil.valueOfMethod("GET"));

        Assert.assertSame(MethodType.GET, RouteUtil.valueOfMethod("get"));

        Assert.assertSame(MethodType.NONE, RouteUtil.valueOfMethod("notExists"));
    }
}
