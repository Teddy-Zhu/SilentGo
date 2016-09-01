package com.silentgo.web.controller;

import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.web.controller
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/29.
 */
@Controller
@Route("/thread/{id:\\d+}/")
public class parentRegextest {

    @Route("/t/{token:[a-zA-Z]+}")
    public String test(Request request, Response response, @PathVariable String token, @PathVariable Integer id) {

        request.setAttribute("data", id + ":" + token);
        return "index.jsp";
    }

    @Route("/t/{token:[0-9]+}")
    public String test(Request request, Response response, @PathVariable int token, @PathVariable Integer id) {

        request.setAttribute("data", id + ":" + token);
        return "index.jsp";
    }
}
