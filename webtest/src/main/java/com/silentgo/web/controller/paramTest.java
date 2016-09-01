package com.silentgo.web.controller;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.servlet.http.Request;
import com.silentgo.web.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.web.controller
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/31.
 */
@Controller
@Route("/param")
public class paramTest {

    @Route("/array")
    public String arraytest(Request request, String[] a) {
        request.setAttribute("data", JSON.toJSONString(a));
        return "index.jsp";
    }
    @Route("/arrayint")
    public String arraytest(Request request, int[] a) {
        request.setAttribute("data", JSON.toJSONString(a));
        return "index.jsp";
    }

    @Route("/liststring")
    public String arraytest(Request request, List<String> a) {
        request.setAttribute("data", JSON.toJSONString(a));
        return "index.jsp";
    }

    @Route("/listint")
    public String arraytest2(Request request, List<Integer> a) {
        request.setAttribute("data", JSON.toJSONString(a));
        return "index.jsp";
    }
}
