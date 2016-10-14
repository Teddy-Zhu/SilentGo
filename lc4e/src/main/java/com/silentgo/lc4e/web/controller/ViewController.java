package com.silentgo.lc4e.web.controller;


import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.render.RenderModel;
import com.silentgo.core.route.annotation.*;
import com.silentgo.lc4e.config.Key;
import com.silentgo.lc4e.entity.Article;
import com.silentgo.lc4e.entity.Message;
import com.silentgo.lc4e.entity.Popup;
import com.silentgo.lc4e.entity.ReturnData;
import com.silentgo.lc4e.tool.Lc4eCaptchaRender;
import com.silentgo.lc4e.tool.RelativeDate;
import com.silentgo.lc4e.web.service.ComVarService;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.RequestMethod;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresGuest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by teddy on 2015/7/18.
 */
@Controller
@Route("/")
public class ViewController {

    private static final Logger log = LoggerFactory.getLog(ViewController.class);

    @Inject
    ComVarService comVarService;

    //    @ValidateParams(value = {
//            @ValidateParam(index = 1, type = int.class, defaultValue = "1"),
//            @ValidateParam(value = "art", type = boolean.class, defaultValue = "false"),
//            @ValidateParam(index = 0, type = Integer.class, defaultValue = "2")
//    })
    @Route("/")
    public String index(Request request, Integer p) {
        request.setAttribute("page", p);
        return "index.html";
    }

    //    @ValidateParams({
//            @ValidateParam(index = 2, type = int.class, defaultValue = "1"), //page
//            @ValidateParam(index = 0, type = String.class, defaultValue = "all"),
//            @ValidateParam(index = 1, type = Integer.class, defaultValue = "1") //order
//    })
    @Route("/a/{area}-{page:[0-9]+}-{order:[0-1]}")
    @RouteMatch(method = RequestMethod.GET)
    public String a(Request request, @PathVariable String area, @PathVariable Integer page, @PathVariable Integer order) {
        request.setAttribute("curArea", area);
        request.setAttribute("topics", getArticle(page, order, area));
        return "index.html";
    }

    @Route("/a/{area}-{page:[0-9]+}-{order:[0-1]}")
    @RouteMatch(method = RequestMethod.POST)
    @ResponseBody
    public Message a2(Request request, @PathVariable String area, @PathVariable Integer page, @PathVariable Integer order) {
        request.setAttribute("curArea", area);
        return new Message(true, new ReturnData("curArea", area),
                new ReturnData("topics", getArticle(page, order, area)));
    }

    @Route
    public RenderModel captcha(Request request, Response response) {
        return new RenderModel(new Lc4eCaptchaRender(), null, request, response);
    }

    @Route
    public String TopHots() {
        return "topHotTest.html";
    }

    @Route
    @RouteMatch(headers = "X-Requested-With=XMLHttpRequest")
    public Message SignOut(Response response) throws IOException {
        SecurityUtils.getSubject().logout();

        return new Message(true, "SignOut Successfully");
    }

    @Route("/SignOut")
    public void SignOut2(Response response) throws IOException {
        SecurityUtils.getSubject().logout();
        response.sendRedirect("/");
    }

    @RequiresGuest
    @Route
    @ResponseBody
    @RouteMatch(method = RequestMethod.POST)
    public Message SignIn() {
        ReturnData data = new ReturnData("Captcha", Boolean.parseBoolean(comVarService.getComVarByName("Captcha").getValue()));
        return new Message(true, data);
    }

    @RequiresGuest
    @Route
    public String SignIn(Response response) {
        return "index.html";
    }

    @RequiresGuest
    @Route
    public String SignUp(Response response) {
        return "index.html";
    }

    @RequiresGuest
    @Route
    @RouteMatch(method = RequestMethod.POST)
    public Message SignUp() {
        ReturnData[] datas = {
                new ReturnData("Captcha", Boolean.parseBoolean(comVarService.getComVarByName("Captcha").getValue())),
                new ReturnData("SimpleRegister", Boolean.parseBoolean(comVarService.getComVarByName("SimpleRegister").getValue()))};

        return new Message(true, datas);
    }

    public List<Article> getArticle(int page, int order, String area) {
        Integer size = 10;
        if (StringKit.isBlank(area) || "all".equals(area)) {
            area = "";
            size = Integer.valueOf(comVarService.getComVarValueByName("IndexPageSize"));
        } else {
            size = Integer.valueOf(comVarService.getComVarValueByName("AreaPageSize"));
        }

        String[] cate = new String[]{"Java", "Obj-C", "C", "C++", "IOS", "Android"};
        String[] high = new String[]{"TOP", "NOTICE", "OTHER", "SYSTEM", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        String[] users = new String[]{"Admin", "Test", "Myas", "Liakx", "Google", "vsss"};
        Date now = new Date();
        List<Article> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new Article("/themes/" + Key.kvs.get("Theme") + "/images/wireframe/image.png", new Popup("Matt", "Matt has been a member since July 2014"), "The friction between your thoughts and your code" + page, "/t/hello" + new Random().nextInt(1000), cate[new Random().nextInt(cate.length - 1)], users[new Random().nextInt(users.length - 1)], new Random().nextInt(100),
                    RelativeDate.format(RelativeDate.randomDate("2015-05-11 13:00:00", now), now), users[new Random().nextInt(users.length - 1)], page, high[new Random().nextInt(high.length - 1)]));
        }
        return list;
    }
}
