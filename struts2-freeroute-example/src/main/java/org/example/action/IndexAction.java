package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 13-2-21 22:22
 */
public class IndexAction {
    @Route("/")
    public String index() {
        return Results.html("/hello.html");
    }
}
