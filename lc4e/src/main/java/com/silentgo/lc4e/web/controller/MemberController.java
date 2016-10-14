package com.silentgo.lc4e.web.controller;


import com.silentgo.core.aop.validator.annotation.RequestBool;
import com.silentgo.core.aop.validator.annotation.RequestString;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.route.annotation.*;
import com.silentgo.lc4e.dao.SysCommonVariable;
import com.silentgo.lc4e.dao.User;
import com.silentgo.lc4e.dao.UserBasicinfo;
import com.silentgo.lc4e.entity.Message;
import com.silentgo.lc4e.tool.Lc4eCaptchaRender;
import com.silentgo.lc4e.web.service.ComVarService;
import com.silentgo.lc4e.web.service.UserService;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.RequestMethod;
import com.silentgo.servlet.http.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 * Created by teddy on 2015/8/10.
 */
@Controller
@Route("/member")
public class MemberController {


    @Inject
    UserService userService;

    @Inject
    ComVarService comVarService;

//    @ValidateParams({
//            @ValidateParam(value = "user.name", minLen = 4, maxLen = 12),
//            @ValidateParam(value = "user.password", minLen = 6, maxLen = 20),
//            @ValidateParam(value = "user.nick", minLen = 4, maxLen = 12),
//            @ValidateParam(value = "user.mail", minLen = 5, maxLen = 30, regex = StringTool.regExp_MAIL),
//            @ValidateParam(value = "extend.phoneNumber", defaultValue = "", regex = StringTool.regExp_PhoneNumber),
//            @ValidateParam(value = "extend.sign", defaultValue = "", maxLen = 50),
//            @ValidateParam(value = "extend.birth", required = false, type = Date.class),
//            @ValidateParam(value = "captcha", defaultValue = "@@@@", maxLen = 4, minLen = 4)
//    })


    @RouteMatch(method = RequestMethod.POST)
    @Route
    @ResponseBody
    public Message signup(Response response, Request request, @RequestParam("user") User user, @RequestParam("extend") UserBasicinfo basicinfo) throws Exception {


        if (!Boolean.parseBoolean(comVarService.getComVarByName("Register").getValue())) {
            return new Message("register closed");
        }
        userService.createUser(user, basicinfo);
        if (user.getId() != null) {
            return new Message(true, "register successfully");
        } else {
            return new Message("register failed");
        }
    }

    //    @ValidateParams({
//            @ValidateParam(value = "user.name", minLen = 4, maxLen = 12),
//            @ValidateParam(value = "user.password", minLen = 6, maxLen = 20),
//            @ValidateParam(value = "captcha", defaultValue = "@@@@", maxLen = 4, minLen = 4),
//            @ValidateParam(value = "rememberMe", type = Boolean.class)
//    })
    @RouteMatch(method = RequestMethod.POST)
    @Route
    @ResponseBody
    public Message signin(@RequestParam("user") User user,
                          @RequestString(defaultValue = "@@@@", range = {4, 4}) String captcha,
                          Request request,
                          @RequestBool Boolean rememberMe) {

        SysCommonVariable captchaComVar = comVarService.getComVarByName("Captcha");
        String captchaValue = captchaComVar == null ? null : captchaComVar.getValue();
        if (captchaValue != null && Boolean.parseBoolean(captchaValue) && !Lc4eCaptchaRender.validate(request, captcha)) {
            return new Message(captchaComVar.getError());
        }
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
            token.setRememberMe(rememberMe);
            subject.login(token);
            subject.getSession().removeAttribute(Lc4eCaptchaRender.captcha_code);
            if (!subject.isAuthenticated()) {
                return new Message("Login failed");
            }
        }
        return new Message(true, "Login Success");
    }

}
