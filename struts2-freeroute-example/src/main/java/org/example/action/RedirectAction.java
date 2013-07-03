package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 13-7-3 下午7:57
 */
public class RedirectAction {
    @Route("/redirect")
    public String toBaidu(){
        return Results.redirect("http://baidu.com");
    }
}
