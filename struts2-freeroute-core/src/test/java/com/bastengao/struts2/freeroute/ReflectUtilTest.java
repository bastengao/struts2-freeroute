package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.ContentBase;
import com.bastengao.struts2.freeroute.annotation.CookieValue;
import com.example.action.BookController;
import com.example.action.ContentBaseController;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author bastengao
 * @date 13-1-7 21:45
 */
public class ReflectUtilTest {
    @Test
    public void testParseAnnotationForProperty() {
        Class clazz = BookController.class;

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (ReflectUtil.isAnnotationPresentOfField(field, CookieValue.class)) {
                CookieValue cookieValue = ReflectUtil.getAnnotationOfField(field, CookieValue.class);
                Assert.assertNotNull(cookieValue);
            }
        }
    }

    @Test
    public void testGetAnnotationOfField() throws NoSuchFieldException {
        Field field = BookController.class.getDeclaredField("name");
        CookieValue cookieValue = ReflectUtil.getAnnotationOfField(field, CookieValue.class);
        Assert.assertNull(cookieValue);
    }

    @Test
    public void testGetAnnotation() {
        ContentBase contentBase = ReflectUtil.getAnnotation(ContentBaseController.class, ContentBase.class);
        Assert.assertNotNull(contentBase);
        Assert.assertEquals("/pages", contentBase.value());

        ContentBase contentBase2 = ReflectUtil.getAnnotation(String.class, ContentBase.class);
        Assert.assertNull(contentBase2);
    }

    @Test
    public void testMethodOf() {
        Class clazz = ReflectUtilTest.class;
        Method method = ReflectUtil.methodOf(clazz, "noExists");

        Assert.assertNull(method);


        method = ReflectUtil.methodOf(clazz, "testMethodOf");

        Assert.assertNotNull(method);
        Assert.assertEquals("testMethodOf", method.getName());
        Assert.assertSame(clazz, method.getDeclaringClass());
    }
}
