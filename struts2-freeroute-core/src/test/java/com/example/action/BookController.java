package com.example.action;

import com.bastengao.struts2.freeroute.annotation.CookieValue;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 12-12-16 20:11
 */
public class BookController {
    @CookieValue("id")
    private int id;

    private String name;

    @Route("/books/show")
    public String execute() {
        return null;
    }
}
