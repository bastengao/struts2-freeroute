package org.example.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author bastengao
 * @date 13-11-23 下午6:35
 */
public class StreamController {

    private InputStream input;

    @Route("/stream-result")
    public String view() throws FileNotFoundException {

        input = new FileInputStream("/path/to/file");

        return Results.stream().inputName("input").done();
    }

    public InputStream getInput() {
        return input;
    }
}
