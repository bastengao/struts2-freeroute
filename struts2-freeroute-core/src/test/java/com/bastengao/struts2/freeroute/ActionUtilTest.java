package com.bastengao.struts2.freeroute;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bastengao
 * @date 12-12-17 23:48
 */
public class ActionUtilTest {
    @Test
    public void testNamespaceFromRoutePath() {
        String namespace = ActionUtil.namespace("/persons/show");
        Assert.assertNotNull(namespace);
        Assert.assertEquals("/persons", namespace);


        namespace = ActionUtil.namespace("persons-show");
        Assert.assertNotNull(namespace);
        Assert.assertEquals("", namespace);
    }

    @Test
    public void testActionNameFromRoutePath() {
        String actionName = ActionUtil.actionName("persons-show");
        Assert.assertNotNull(actionName);
        Assert.assertEquals("persons-show", actionName);

        actionName = ActionUtil.actionName("/persons/show");
        Assert.assertNotNull(actionName);
        Assert.assertEquals("show", actionName);
    }

    @Test
    public void testPadSlash() {
        Assert.assertEquals("/", ActionUtil.padSlash(""));

        Assert.assertEquals("/", ActionUtil.padSlash("/"));

        Assert.assertEquals("/abc", ActionUtil.padSlash("/abc"));

        Assert.assertEquals("/abc", ActionUtil.padSlash("abc"));
    }

    @Test
    public void testShrinkEndSlash() {
        Assert.assertEquals("/abc", ActionUtil.shrinkEndSlash("/abc/"));
        Assert.assertEquals("/abc", ActionUtil.shrinkEndSlash("/abc"));

        Assert.assertEquals("", ActionUtil.shrinkEndSlash("/"));
        Assert.assertEquals("", ActionUtil.shrinkEndSlash(""));
    }
}
