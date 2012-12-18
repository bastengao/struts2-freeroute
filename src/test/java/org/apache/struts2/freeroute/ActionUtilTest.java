package org.apache.struts2.freeroute;

import org.apache.struts2.freeroute.MyPackageProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author bastengao
 * @date 12-12-17 23:48
 */
public class ActionUtilTest {
    @Test
    public void testNamespaceFromRoutePath() {
        String namespace = MyPackageProvider.namespace("/persons/show");
        Assert.assertNotNull(namespace);
        Assert.assertEquals("/persons", namespace);


        namespace = MyPackageProvider.namespace("persons-show");
        Assert.assertNotNull(namespace);
        Assert.assertEquals("", namespace);
    }

    @Test
    public void testActionNameFromRoutePath() {
        String actionName = MyPackageProvider.actionName("persons-show");
        Assert.assertNotNull(actionName);
        Assert.assertEquals("persons-show", actionName);

        actionName = MyPackageProvider.actionName("/persons/show");
        Assert.assertNotNull(actionName);
        Assert.assertEquals("show", actionName);
    }

    @Test
    public void testPadSlash() {
        Assert.assertEquals("/", MyPackageProvider.padSlash(""));

        Assert.assertEquals("/", MyPackageProvider.padSlash("/"));

        Assert.assertEquals("/abc", MyPackageProvider.padSlash("/abc"));

        Assert.assertEquals("/abc", MyPackageProvider.padSlash("abc"));
    }
}
