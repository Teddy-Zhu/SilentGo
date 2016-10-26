package com.silentgo.test.controller;


import com.silentgo.core.SilentGo;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.bean.SilentGoBeanFactory;
import com.silentgo.core.route.annotation.*;
import com.silentgo.servlet.http.RequestMethod;
import com.silentgo.test.model.Menu;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.test.controller
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/26.
 */
@Controller
@Route("/")
public class view {

    private static final Logger LOGGER = LoggerFactory.getLog(view.class);

    @Inject
    SilentGoBeanFactory silentGoBeanFactory;

    @Route("/")
    public String index() {
        return "index.jsp";
    }

    @Route("/{string}")
    @ResponseBody
    public String test(@PathVariable String string) {
        LOGGER.info("msg:{}", string);
        return string;
    }

    @Route("/{string}")
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST)
    public String testpost(@PathVariable String string) {
        return "post method :" + string;
    }

    @Route("/{string}")
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST, params = "name=1")
    public String testpost2(@PathVariable String string) {
        return "post method name 1:" + string;
    }

    @Route("/aaa")
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST)
    public String testpostxxx2(@RequestBody String string) {
        return "request body" + string;
    }

    @Route("/xxxxx")
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST)
    public String testpoasdstxxx2(@RequestParam @RequestBody String string) {
        return "request body name " + string;
    }

    @Route("/abcdefg")
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST)
    public String abc(@RequestParam @RequestBody Menu menu) {
        return "request body object" + SilentGo.me().json().toJsonString(menu);
    }

    @Route("/array1")
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST)
    public String testpostxxx2(@RequestBody String[] string) {
        return "request body array " + string;
    }

    @Route("/array2")
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST)
    public String testpostxxx2(@RequestParam("m") @RequestBody List<Menu> menu) {
        return "request body object list" + SilentGo.me().json().toJsonString(menu);
    }

    @Route("/array3")
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST)
    public String testpostxxxx2(@RequestBody List<Menu> menu) {
        return "request body object list" + SilentGo.me().json().toJsonString(menu);
    }
}
