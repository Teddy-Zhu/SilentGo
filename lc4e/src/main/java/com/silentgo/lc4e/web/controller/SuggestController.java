package com.silentgo.lc4e.web.controller;


import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.route.annotation.Controller;
import com.silentgo.core.route.annotation.Route;
import com.silentgo.lc4e.dao.User;
import com.silentgo.lc4e.entity.Message;
import com.silentgo.lc4e.web.service.UserService;
import com.silentgo.utils.StringKit;

/**
 * Created by teddy on 2015/8/27.
 */
@Controller
@Route("/su")
public class SuggestController {

    @Inject
    UserService userService;

    //    @ValidateParams(value = {
//            @ValidateParam(value = "user.name", minLen = 4, maxLen = 12, required = false, defaultValue = ""),
//            @ValidateParam(value = "user.nick", minLen = 4, maxLen = 12, required = false, defaultValue = ""),
//            @ValidateParam(value = "user.mail", minLen = 6, maxLen = 30, required = false, defaultValue = "")})
    @Route
    public Message user(User user) {
        boolean exist = true;
        StringBuilder message = new StringBuilder();
        if (StringKit.isNotBlank(user.getName())) {
            exist = userService.validateUserName(user.getName());
            message.append("UserName");
        } else if (StringKit.isNotBlank(user.getNick())) {
            exist = userService.validateUserNick(user.getNick());
            message.append("UserNick");
        } else if (StringKit.isNotBlank(user.getMail())) {
            exist = userService.validateUserMail(user.getMail());
            message.append("UserMail");
        }
        message.append(" Has been occupied");
        return new Message(!exist, exist ? message.toString() : "");
    }
}
