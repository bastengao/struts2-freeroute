package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 13-1-12 16:18
 */
public class HttpHeaderController {
    @Route("/http-header")
    public String show() {
        return Results.httpHeader()
                .status("204")
                .headers("Xname", "bastengao")
                .done();
    }
}

