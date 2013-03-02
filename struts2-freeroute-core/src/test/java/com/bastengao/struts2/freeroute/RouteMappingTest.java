package com.bastengao.struts2.freeroute;

import com.bastengao.struts2.freeroute.annotation.Route;
import org.junit.Assert;
import org.junit.Test;

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
}
