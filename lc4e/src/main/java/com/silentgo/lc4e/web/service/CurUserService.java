package com.silentgo.lc4e.web.service;

import com.silentgo.core.SilentGo;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.lc4e.dao.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by teddy on 2015/9/23.
 */
@Service
public class CurUserService {

    public String getSessionId() {
        Subject user = SecurityUtils.getSubject();
        if (user == null) {
            return null;
        }
        return user.getSession().getId().toString();
    }

    public User getCurrentUser() {
        Subject user = SecurityUtils.getSubject();
        if (user == null) {
            return null;
        }
        User curUser = null;
        if (user.isAuthenticated()) {
            curUser = (User) SilentGo.getInstance().getConfig().getCacheManager().get("users", user.getSession().getId().toString());
        }
        return curUser;
    }

    public boolean isLogin() {
        Subject user = SecurityUtils.getSubject();
        return user != null && user.isAuthenticated();
    }
}
