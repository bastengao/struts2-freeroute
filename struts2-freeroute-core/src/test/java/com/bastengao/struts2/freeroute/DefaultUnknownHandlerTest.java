package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.ContentBase;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.bastengao.struts2.freeroute.helper.RouteHelper;
import com.example.action.BookController;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.text.AbstractDocument;
import java.lang.reflect.Method;

/**
 * @author bastengao
 * @date 13-1-14 23:02
 */
public class DefaultUnknownHandlerTest {

    @Test
    public void testParseResultType() {
        String resultType = DefaultUnknownHandler.parseResultType("dispatcher");
        Assert.assertEquals("dispatcher", resultType);

        resultType = DefaultUnknownHandler.parseResultType("dispatcher:abc.html");
        Assert.assertEquals("dispatcher", resultType);
    }

    @Test
    public void testIsJSONObject() {
        boolean result = DefaultUnknownHandler.isJSONObject("{\"name\":\"value\"}");
        Assert.assertTrue(result);

        result = DefaultUnknownHandler.isJSONObject("{}");
        Assert.assertTrue(result);

        result = DefaultUnknownHandler.isJSONObject("");
        Assert.assertFalse(result);
    }

    @Test
    public void testParsePath() {
        String location = "/absolute-path.html";
        String path = DefaultUnknownHandler.parsePath(null, null, location);
        Assert.assertEquals(path, location);
    }

    @Test
    public void testParsePath2A() {
        ContentBase contentBase = Mockito.mock(ContentBase.class);
        Mockito.when(contentBase.value()).thenReturn("/content-base");
        Route route = RouteHelper.mockRoute("/path");
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        RouteMapping routeMapping = new RouteMapping(contentBase, route, clazz, method);

        String location = "path.html";
        String path = DefaultUnknownHandler.parsePath(null, routeMapping, location);

        Assert.assertEquals("/content-base/path.html", path);
    }

    @Test
    public void testParsePath2B() {
        ContentBase contentBase = Mockito.mock(ContentBase.class);
        Mockito.when(contentBase.value()).thenReturn("/content-base/");
        Route route = RouteHelper.mockRoute("/path");
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        RouteMapping routeMapping = new RouteMapping(contentBase, route, clazz, method);

        String location = "path.html";
        String path = DefaultUnknownHandler.parsePath(null, routeMapping, location);

        Assert.assertEquals("/content-base/path.html", path);
    }

    @Test
    public void testParsePath2C() {
        ContentBase contentBase = Mockito.mock(ContentBase.class);
        Mockito.when(contentBase.value()).thenReturn("content-base");
        Route route = RouteHelper.mockRoute("/path");
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        RouteMapping routeMapping = new RouteMapping(contentBase, route, clazz, method);

        String location = "path.html";
        String path = DefaultUnknownHandler.parsePath(null, routeMapping, location);

        Assert.assertEquals("/content-base/path.html", path);
    }

    @Test
    public void testParsePath2D() {
        ContentBase contentBase = Mockito.mock(ContentBase.class);
        Mockito.when(contentBase.value()).thenReturn("content-base/");
        Route route = RouteHelper.mockRoute("/path");
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        RouteMapping routeMapping = new RouteMapping(contentBase, route, clazz, method);

        String location = "path.html";
        String path = DefaultUnknownHandler.parsePath(null, routeMapping, location);

        Assert.assertEquals("/content-base/path.html", path);
    }

    @Test
    public void testParsePath3A() {
        Route route = RouteHelper.mockRoute("/path");
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        String globalContentBase = "/global-content-base";
        String location = "path.html";
        String path = DefaultUnknownHandler.parsePath(globalContentBase, routeMapping, location);

        Assert.assertEquals("/global-content-base/path.html", path);
    }

    @Test
    public void testParsePath4() {
        Route route = RouteHelper.mockRoute("/path");
        Class clazz = BookController.class;
        Method method = ReflectUtil.methodOf(clazz, "execute");

        RouteMapping routeMapping = new RouteMapping(route, clazz, method);

        String location = "path.html";
        String path = DefaultUnknownHandler.parsePath(null, routeMapping, location);

        Assert.assertEquals("/path.html", path);
    }

}
