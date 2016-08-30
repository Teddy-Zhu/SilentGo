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
@Route("/re")
public class regexTestController {

    @Route("/{user}/{password}")
    public String test(Request request, @PathVariable String user, @PathVariable String password, Response response) {

        request.setAttribute("data", user + ":" + password);
        return "index.jsp";
    }
}
