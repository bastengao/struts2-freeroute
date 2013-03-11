package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.ControllerPackage;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 13-3-10 下午4:34
 */
@ControllerPackage(parent = "pkg-2")
public class CustomPackageAction {

    @Route("/custom-package")
    public String view() {
        return Results.html("/my-result.html");
    }

    @Route(value = "/custom-interceptor", interceptors = {"myInterceptor", "my2Interceptor"})
    public String view2() {
        return Results.html("/my-result.html");
    }
}
