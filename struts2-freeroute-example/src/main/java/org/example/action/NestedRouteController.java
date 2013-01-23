package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 *
 * @author bastengao
 * @date 13-1-21 23:10
 */
@Route("/nested-route")
public class NestedRouteController {
    // GET /nested-route
    @Route(method = MethodType.GET)
    public String index() {
        return Results.html("/my-result");
    }

    // POST /nested-route
    @Route(method = MethodType.POST)
    public String add() {
        return Results.html("/my-result");
    }

    private String id;

    // NONE /nested-route/{id}/edit
    @Route("/{id}/edit")
    public String edit() {
        System.out.println(id);
        return Results.html("/my-result");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
