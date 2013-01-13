package com.bastengao.struts2.freeroute;

import com.example.action.BookController;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author bastengao
 * @date 12-12-16 17:48
 */
public class ControllerPackageProviderTest {
    @Test
    public void testFindController() throws IOException {
        Set<ClassPath.ClassInfo> controllerClassInfos = ControllerPackageProvider.findControllers("com.example", Sets.newHashSet("Controller"));
        Assert.assertNotNull(controllerClassInfos);
        Assert.assertFalse(controllerClassInfos.isEmpty());
    }

    @Test
    public void testParseController() throws IOException {
        for (ClassPath.ClassInfo classInfo : ControllerPackageProvider.findControllers("com.example", Sets.newHashSet("Controller"))) {
            ControllerPackageProvider.parseController(classInfo.load());
        }
    }

    @Test
    public void testParseController2() {
        List<RouteMapping> routings = ControllerPackageProvider.parseController(BookController.class);
        Assert.assertEquals(1, routings.size());

        RouteMapping routeMapping = routings.get(0);
        Assert.assertEquals("/books/show", routeMapping.getRoute().value());
        Assert.assertEquals("execute", routeMapping.getMethod().getName());
    }
}
