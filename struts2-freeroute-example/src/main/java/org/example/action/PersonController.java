package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.ContentBase;
import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;
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

    @Route(value = "/persons/create", method = MethodType.GET)
    public String toCreate() {
        // "dispatcher:/my-result.html";
        return Results.html("/my-result.html");
    }

    @Route(value = "/persons/create", params = "a")
    public String toCreate3() {
        // "/my-result.jsp"
        return Results.jsp("/my-result");
    }

    @Route("/persons/create2")
    public String toCreate2() {
        return "dispatcher:/my-result2.html";
    }

    @Route(value = "/persons/create2", method = MethodType.POST)
    public String toCreate2Post() {
        System.out.println("create2 post");
        return "dispatcher:/my-result2.html";
    }

    // {id} value in path will be set to this#id property.
    @Route(value = "/persons/{id}", method = MethodType.GET)
    public String show() {
        //will render by "/pages/content.html"
        System.out.println(id);
        return "freemarker:/my-result.ftl";
    }

    // {id} value in path will be set to this#id property.
    @Route(value = "/persons/{id}", method = {MethodType.GET, MethodType.POST}, params = {"_method"})
    public String show2() {
        //will render by "/pages/content.html"
        System.out.println(id);
        System.out.println("post");
        return "freemarker:/my-result.ftl";
    }

    @Route(value = "/persons/{id}", method = MethodType.GET, params = "redirect=true")
    public String showRedirect() {
        return "redirect:/persons/" + id + ".json";
    }

    @Route(value = "/persons/{id}", method = MethodType.GET, params = {"redirect=true", "relative"})
    public String showRedirect2(){
        return "redirect:" + id + ".json";
    }

    @Route("/persons/{id}.json")
    public String showJson() {
        return Results.json().done();
    }

    @Route("/persons/{id}.xml")
    public String showXml() {
        System.out.println(id);
        return "dispatcher:/my-result.html";
    }

    @Route("/persons/{id}.jsp")
    public String showJsp() {
        System.out.println(id);
        return "dispatcher:/my-result.jsp";
    }

    @Route("/{id}/edit")
    public String edit() {
        return "dispatcher:content.html";
    }

    @Route(value = "/persons/{id}", method = MethodType.PUT, params = {"_method"})
    public String update() {
        System.out.println("update");
        return "dispatcher:/my-result.jsp";
    }

    @Route(value = "/persons/{id}", method = MethodType.DELETE, params = {"_method=delete", "foo!=bar"})
    public String delete() {
        System.out.println("delete");
        return "dispatcher:/my-result.jsp";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
