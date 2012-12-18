package org.apache.struts2.freeroute;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bastengao
 * @date 12-12-18 23:29
 */
public class RouteUtilTest {

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
        // "^([?:/|\w]*/{[(a-zA-Z)]+})+$"
        //Pattern pattern = Pattern.compile("^([/|\\w]*/\\{[a-zA-Z]+\\})+$");
        Pattern pattern = Pattern.compile("^(?>[/|\\w]*/\\{([a-zA-Z]+)\\})+$");

        String path = "/{id}";
        Assert.assertTrue(pattern.matcher("/{id}").matches());
//        System.out.println(pattern.matcher("/{id}").groupCount());
//        groups(pattern.matcher("/{id}"));

        Assert.assertTrue(pattern.matcher("/persons/{id}").matches());
//        System.out.println(pattern.matcher("/persons/{id}").groupCount());

        Assert.assertTrue(pattern.matcher("/persons/{id}/edit/{name}").matches());
//        System.out.println((pattern.matcher("/persons/{id}/edit/{name}").groupCount()));
        groups(pattern.matcher("/persons/{id}/edit/{name}"));

        Assert.assertFalse(pattern.matcher("{id}").matches());
        Assert.assertFalse(pattern.matcher("/persons/{}").matches());
        Assert.assertFalse(pattern.matcher("/persons/{id}/").matches());
    }

    private void groups(Matcher matcher) {
        if (matcher.lookingAt()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }

    @Test
    public void testRegexp() {
        Pattern pattern = Pattern.compile("(?>([0-9]+)([a-z]+))+");
        Matcher matcher = pattern.matcher("123abc456xyz");
        System.out.println(matcher.matches());
        for (int i = 1; i <= matcher.groupCount(); i++) {
            System.out.println(matcher.group(i));
        }
    }
}
