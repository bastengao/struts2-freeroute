package org.example.action;

import com.bastengao.struts2.freeroute.Result;
import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author bastengao
 * @date 13-1-12 20:21
 */
@Component("myBook2Action")
@Scope("prototype")
public class Book2Action {

    @Route("/books2")
    public String execute() {
        return Result.create().location("content.html").done();
    }

    @Route(value = "/books/{id}", params = "any")
    public String show() {
        return Results.html("content");
    }
}
