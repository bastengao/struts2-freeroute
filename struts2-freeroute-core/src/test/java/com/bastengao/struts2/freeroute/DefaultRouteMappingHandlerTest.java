package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.bastengao.struts2.freeroute.helper.RouteHelper;
import com.example.action.BookController;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bastengao
 * @date 12-12-22 22:50
 */
public class DefaultRouteMappingHandlerTest {
    @Test
    public void testPatternEquals() {
        Pattern a = Pattern.compile("abc");
        Pattern b = Pattern.compile("abc");

        Assert.assertNotEquals(a, b);
    }

    @Test
    public void testPatternEquals2() {
        Pattern a = Pattern.compile("abc");
        Pattern b = Pattern.compile("abc");

        Assert.assertEquals(2, Sets.newHashSet(a, b).size());

        Assert.assertEquals(1, Sets.newHashSet("abc", "abc").size());
    }

    @Test
    public void testWeightOfMethod() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path");
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfMethod(request, routeMapping);
        Assert.assertEquals(1000, weight);
    }

    @Test
    public void testWeightOfMethod2() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.GET}, new String[]{});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfMethod(request, routeMapping);
        Assert.assertEquals(1001, weight);
    }

    @Test
    public void testWeightOfMethod3() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.GET, MethodType.POST}, new String[]{});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfMethod(request, routeMapping);
        Assert.assertEquals(1001, weight);
    }

    @Test
    public void testContainsSameRoutePath() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path");
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path");
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNotNull(sameRoute);
    }

    @Test
    public void testContainsSameRoutePath2() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path");
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path2");
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNull(sameRoute);
    }

    @Test
    public void testContainsSameRouteHttpMethod() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.GET}, new String[]{});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.GET}, new String[]{});
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNotNull(sameRoute);
    }

    @Test
    public void testContainsSameRouteHttpMethod2() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.GET}, new String[]{});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.POST}, new String[]{});
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNull(sameRoute);
    }

    @Test
    public void testContainsSameRouteHttpMethod3() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.GET}, new String[]{});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.POST, MethodType.GET}, new String[]{});
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNotNull(sameRoute);
    }

    @Test
    public void testContainsSameRouteHttpParam() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param"});
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNotNull(sameRoute);
    }

    @Test
    public void testContainsSameRouteHttpParam2() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param2"});
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNull(sameRoute);
    }

    @Test
    public void testContainsSameRouteHttpParam3() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param2!=value", "param"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param", "param2!=value"});
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNotNull(sameRoute);
    }

    @Test
    public void testContainsSameRoute() {
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.GET}, new String[]{"param2!=value", "param"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        Route route2 = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.POST, MethodType.GET}, new String[]{"param", "param2!=value"});
        RouteMapping routeMapping2 = new RouteMapping(route2, clazz, method);


        RouteMapping sameRoute = DefaultRouteMappingHandler.containsSameRoute(routeMapping2, Lists.newArrayList(routeMapping));
        Assert.assertNotNull(sameRoute);
    }

}
