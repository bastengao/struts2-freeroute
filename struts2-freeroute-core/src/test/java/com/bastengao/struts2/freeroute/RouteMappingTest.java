package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.Route;
import com.bastengao.struts2.freeroute.helper.RouteHelper;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author bastengao
 * @date 13-3-2 09:48
 */
public class RouteMappingTest {

    @Test
    public void mockAnnotationTest() {
        Route route = mock(Route.class);
        when(route.value()).thenReturn("/path");

        Assert.assertEquals("/path", route.value());
    }

    @Test
    public void newRouteMapping() {
        Route route = RouteHelper.mockRoute("/path");

        Class clazz = RouteMappingTest.class;
        Method method = ReflectUtil.methodOf(clazz, "newRouteMapping");

        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Assert.assertSame(clazz, routeMapping.getAction());
        Assert.assertSame(method, routeMapping.getMethod());
        Assert.assertEquals(0, routeMapping.getHttpMethods().size());
        Assert.assertEquals(0, routeMapping.getHttpParams().size());
        Assert.assertFalse(routeMapping.hasPathVariables());

        ActionInfo actionInfo = routeMapping.toAction();
        Assert.assertNotNull(actionInfo);
        Assert.assertEquals("", actionInfo.getNamespace());
        Assert.assertEquals("path" + "#" + method.getName() + "@" + clazz.getName(), actionInfo.getActionName());
    }

}
