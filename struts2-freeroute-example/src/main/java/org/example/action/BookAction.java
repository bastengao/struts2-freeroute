package org.example.action;

import com.bastengao.struts2.freeroute.Result;
import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 13-1-12 20:21
 */
public class BookAction {

    private int id;

    @Route("/books")
    public String execute() {
        return Result.create().location("content.html").done();
    }

    @Route("/books")
    public String execute2() {
        return Result.create().location("content.html").done();
    }

    @Route("/books/{id}")
    public String show() {
        System.out.println(id);
        return Results.html("content");
    }

    @Route("/books/{id}.json")
    public String showJson() {
        System.out.println("json: " + id);
        return Results.json().done();
    }

    @Route("/books/{id}/{method}")
    public String showByMethod() {
        return Results.html("content");
    }

    @Route("/books/123/{method}")
    public String showStatic() {
        return Results.html("content");
    }

    @Route(value = "/books/123/{method}", params = "any")
    public String showStatic2() {
        return Results.html("content");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
