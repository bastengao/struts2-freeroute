package org.example.action;

import java.lang.String;
import com.bastengao.struts2.freeroute.annotation.ContentBase;
import com.bastengao.struts2.freeroute.annotation.Route;

/**
 * @author bastengao
 * @date 12-12-28 22:16
 */
@ContentBase("/pages")
public class ContentBaseController {

    @Route("/test-content-base")
    public String view() {
        // "dispatcher:/pages/content.html"
        return "dispatcher:content.html";
    }

    @Route("/another-absolute-path")
    public String view2() {
        // "/" 开始将使用绝对路径, 如果则使用相对路径
        return "dispatcher:/pages/content.html";
    }
}
