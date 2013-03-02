package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.CookieValue;
import com.example.action.BookController;
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
