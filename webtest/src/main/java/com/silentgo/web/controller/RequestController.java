package com.silentgo.web.controller;

import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.RequestBody;
import com.silentgo.core.route.annotation.ResponseBody;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.web.model.User;

/**
 * Project : silentgo
 * com.silentgo.web.controller
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/30.
 */
@Controller
@Route("/")
public class RequestController {
    @Route("/")
    @ResponseBody
    public String testindex(Request request) {
        return "index.jsp";
    }

    @Route("/test/user")
    public String test(Request request, @RequestBody User user) {
        request.setAttribute("data", user.toString());
        return "index.jsp";
    }

    @ExceptionHandler
    public void testExceptionHandle(Exception e, Response response) {
        System.out.println(e.getMessage());
    }
}
