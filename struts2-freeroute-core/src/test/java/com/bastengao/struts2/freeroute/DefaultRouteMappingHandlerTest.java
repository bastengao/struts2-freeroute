package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.bastengao.struts2.freeroute.helper.RouteHelper;
import com.example.action.BookController;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.regex.Pattern;

import static org.mockito.Mockito.*;

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
    public void testWeightOfMethod4() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{MethodType.POST}, new String[]{});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfMethod(request, routeMapping);
        Assert.assertEquals(-1, weight);
    }

    @Test
    public void testWeightOfParams() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(0, weight);
    }

    @Test
    public void testWeightOfParams2A() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param")));

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(2, weight);
    }

    @Test
    public void testWeightOfParams2B() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param")));

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param2"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(-1, weight);
    }

    @Test
    public void testWeightOfParams3A() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param", "param2", "param3")));

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param", "param2", "param3"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(6, weight);
    }

    @Test
    public void testWeightOfParams3B() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param", "param2", "param3")));

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param0", "param2", "param3"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(-1, weight);
    }

    @Test
    public void testWeightOfParams4A() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param")));
        when(request.getParameter("param")).thenReturn("value");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param=value"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(2, weight);
    }

    @Test
    public void testWeightOfParams4B() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param")));
        when(request.getParameter("param")).thenReturn("not-value");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param=value"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(-1, weight);
    }

    @Test
    public void testWeightOfParams5A() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param")));
        when(request.getParameter("param")).thenReturn("value2");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param!=value"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(2, weight);
    }

    @Test
    public void testWeightOfParams5B() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param")));
        when(request.getParameter("param")).thenReturn("value");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param!=value"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(-1, weight);
    }

    @Test
    public void testWeightOfParams6() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterNames()).thenReturn(toEnumeration(Arrays.asList("param", "param2", "param3")));
        when(request.getParameter("param2")).thenReturn("value");
        when(request.getParameter("param3")).thenReturn("value2");

        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        Route route = RouteHelper.mockRoute("/path", new MethodType[]{}, new String[]{"param","param2=value", "param3!=value"});
        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        int weight = DefaultRouteMappingHandler.weightOfParams(request, routeMapping);
        Assert.assertEquals(6, weight);
    }

    @Test
    public void testMockEnumeration() {
        Enumeration<String> enumeration = mock(Enumeration.class);

        doAnswer(new Answer() {
            int maxTimes = 2;
            int times = -1;

            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                times++;
                return (times < maxTimes);
            }
        }).when(enumeration).hasMoreElements();

        doAnswer(new Answer() {
            int times = 0;

            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return String.valueOf(times++);
            }
        }).when(enumeration).nextElement();

        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }
    }

    private static <T> Enumeration<T> toEnumeration(final Collection<T> collection) {
        return new Enumeration<T>() {
            private Iterator<T> it = collection.iterator();

            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public T nextElement() {
                return it.next();
            }
        };
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
