package com.silentgo.web.controller;

import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.web.testInject.bInter;

/**
 * Project : silentgo
 * com.silentgo.web.controller
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/29.
 */
@Controller
public class noRouteController {

    @Inject
    bInter bInter;

    @Inject("am")
    bInter bInter2;

    @Route("/t/{id}.html")
    public String test(@PathVariable String id, Request request, Response response) {
        bInter.say(id);
        bInter2.say(id);
        request.setAttribute("data", "aaaa");
        return "index.jsp";
    }
}
