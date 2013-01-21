package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * TODO 实现 issue #7
 *
 * @author bastengao
 * @date 13-1-21 23:10
 */
@Route("/nested-route")
public class NestedRouteController {
    // GET /nested-route
    @Route(method = MethodType.GET)
    public String index() {
        return Results.html("my-result");
    }

    // POST /nested-route
    @Route(method = MethodType.POST)
    public String add() {
        return Results.html("my-result");
    }
}
