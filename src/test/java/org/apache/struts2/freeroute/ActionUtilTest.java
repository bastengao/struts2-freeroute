package org.apache.struts2.freeroute;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bastengao
 * @date 12-12-17 23:48
 */
public class ActionUtilTest {
    @Test
    public void testNamespaceFromRoutePath() {
        String namespace = ControllerPackageProvider.namespace("/persons/show");
        Assert.assertNotNull(namespace);
        Assert.assertEquals("/persons", namespace);


        namespace = ControllerPackageProvider.namespace("persons-show");
        Assert.assertNotNull(namespace);
        Assert.assertEquals("", namespace);
    }

    @Test
    public void testActionNameFromRoutePath() {
        String actionName = ControllerPackageProvider.actionName("persons-show");
        Assert.assertNotNull(actionName);
        Assert.assertEquals("persons-show", actionName);

        actionName = ControllerPackageProvider.actionName("/persons/show");
        Assert.assertNotNull(actionName);
        Assert.assertEquals("show", actionName);
    }

    @Test
    public void testPadSlash() {
        Assert.assertEquals("/", ControllerPackageProvider.padSlash(""));

        Assert.assertEquals("/", ControllerPackageProvider.padSlash("/"));

        Assert.assertEquals("/abc", ControllerPackageProvider.padSlash("/abc"));

        Assert.assertEquals("/abc", ControllerPackageProvider.padSlash("abc"));
    }
}
