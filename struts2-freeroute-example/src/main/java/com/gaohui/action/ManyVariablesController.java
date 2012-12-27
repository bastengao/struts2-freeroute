package com.gaohui.action;

import org.apache.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 12-12-19 23:30
 */
public class ManyVariablesController {
    private int id;
    private String name;
    private String title;

    @Route("/persons/{id}/name/{name}/title/{title}")
    public String execute() {
        System.out.printf("id:%s, name:%s, title:%s%n", id, name, title);
        return "my-result";
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
