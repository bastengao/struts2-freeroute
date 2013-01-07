package org.example.action;

import com.bastengao.struts2.freeroute.annotation.CookieValue;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 13-1-7 21:10
 */
public class CookieController {
    @CookieValue("id")
    private int id;

    private String name;

    @Route("/cookie/{name}")
    public String show() {
        System.out.printf("id: %d, name: %s%n", id, name);
        return "dispatcher:/my-result.html";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
