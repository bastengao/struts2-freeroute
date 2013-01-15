package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bastengao
 * @date 13-1-10 21:57
 */
public class JsonController {
    private Map<String, Object> values = new HashMap<String, Object>();

    @Route("/json-result")
    public String show() {
        values.put("name", "bastengao");
        return Results.json().root("values").noCache(true).done();
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }
}
