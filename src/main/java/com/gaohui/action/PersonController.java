package com.gaohui.action;

import org.apache.struts2.freeroute.annotation.ContentBase;
import org.apache.struts2.freeroute.annotation.MethodType;
import org.apache.struts2.freeroute.annotation.Route;
import com.opensymphony.xwork2.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bastengao
 * @date 12-12-16 15:12
 */
@ContentBase("/pages")
public class PersonController {
    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private int id;

    @Route("/persons/create")
    public String toCreate() {
        log.debug("toCreate:{}", id);
        return Action.SUCCESS;
    }

    @Route("/create")
    public String toCreate1() {
        return Action.SUCCESS;
    }

    @Route("/persons/create2")
    public String toCreate2() {
        return null;
    }

    @Route("/persons/nested/create3")
    public String toCreate3() {
        return null;
    }

    @Route(value = "new", method = MethodType.POST)
    public String create() {
        return "content";
    }

    // {id} value in path will be set to this#id property.
    //@Route(value = "/{id}", method = MethodType.GET)
    public String show() {
        //will render by "/pages/content.html"
        return "content";
    }

    //@Route("/{id}/edit")
    public String edit() {
        return "content";
    }

    //@Route(value = "/{id}", method = MethodType.PUT)
    public String update() {
        return "content";
    }

    //@Route(value = "/{id}", method = MethodType.DELETE)
    public String delete() {
        return "content";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
