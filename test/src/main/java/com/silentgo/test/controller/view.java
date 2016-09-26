package com.silentgo.test.controller;


import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.core.route.annotation.ResponseBody;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

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
}
