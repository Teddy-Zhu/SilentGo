package com.silentgo.web.controller;

import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.web.model.User;

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
    public void index2(Response response, Request request, int size) {
        System.out.println(size);
    }

    @Route
    public void index(Response response, Request request, Integer size) {
        System.out.println(size);
    }

    @Route("month")
    public void cc(Response response, Request request, String name) {
        System.out.println(name);
    }

    @Route("route/{id}/{a}")
    public String testRegex(@PathVariable("id") String a, Request request) {
        LOGGER.debug(a);
        LOGGER.debug(request.getPathParameter("id"));
        return "index.html";
    }

    @Route("match/{:\\d+}/{a}")
    public String testRegexxx(@PathVariable("a") String a, Request request) {
        LOGGER.debug(a);
        LOGGER.debug(request.getPathParameter("id"));
        LOGGER.debug(request.getPathParameter(0));
        return "index.html";
    }

    @Route("user/{username}/{password}")
    public String getUser(User user, Request request) {
        LOGGER.debug(user == null ? "" : user.toString());
        return "index.html";
    }


    @Route("route/regex/{id:\\d+}")
    public void testRegex2(Request request) {
        LOGGER.debug(request.getPathParameter("id"));
    }


    @Route("/regex/{id:[a-z0-9]+}")
    public void testRegex2x(Request request, Response response) {
        LOGGER.debug(request.getPathParameter("id"));
    }
}
