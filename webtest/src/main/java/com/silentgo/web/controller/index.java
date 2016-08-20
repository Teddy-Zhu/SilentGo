package com.silentgo.web.controller;

import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.web.controller
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/19.
 */
@Controller("u")
public class index {

    private Logger LOGGER = LoggerFactory.getLog(index.class);

    @Route
    public void index(Response response, Request request, Integer size) {
        System.out.println(size);
    }

    @Route("month")
    public void cc(Response response, Request request, String name) {
        System.out.println(name);
    }

    @Route("route/{id}/{a}")
    public void testRegex(@PathVariable("id") String a, Request request) {
        System.out.println(a);
        System.out.println(request.getPathNamedParameter("id"));
    }

    @Route("route/regex/{id:d+}")
    public void testRegex2(Request request) {
        System.out.println(request.getPathNamedParameter("id"));
    }
}
