package com.silentgo.lc4e.web.service;

import com.silentgo.core.cache.annotation.Cache;
import com.silentgo.core.db.intercept.Transaction;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.lc4e.config.Key;
import com.silentgo.lc4e.dao.*;
import com.silentgo.lc4e.util.shiro.PassDisposer;
import com.silentgo.utils.Assert;

import java.util.Date;
import java.util.List;

/**
 * Created by teddy on 2015/8/7.
 */
@Service
public class UserService {

    @Inject
    UserDao userDao;

    @Inject
    VwUserRolePermissionDao vwUserRolePermissionDao;
    @Inject
    ComVarService comVarService;

    @Inject
    UserBasicinfoDao userBasicinfoDao;

    @Cache(cacheName = Key.ComVar, index = 0)
    public List<VwUserRolePermission> findUserRolesAndPermission(String username) {

        VwUserRolePermission query = new VwUserRolePermission();
        query.setName(username);
        query.setRoleEndTime(new Date());
        query.setPermissionAvailable(1);
        query.setRoleAvailable(1);

        return vwUserRolePermissionDao.queryByModelSelective(query);
    }

    @Cache(cacheName = Key.ComVar, index = 0)
    public User findUserFullInfo(String username) {
        return userDao.queryOneByName(username);
    }

    // to do simply
    public boolean validateUserName(String name) {
        return userDao.countByName(name) > 0;
    }

    public boolean validateUserNick(String nick) {
        return userDao.countByNick(nick) > 0;
    }

    public boolean validateUserMail(String mail) {
        return userDao.countByMail(mail) > 0;
    }

    @Transaction
    public User createUser(User user, UserBasicinfo basicinfo) throws Exception {
        //validate exist
        //username
        if (validateUserName(user.getName())) {
            throw new Exception("User Name Has been occupied");
        }
        //usernick
        if (validateUserMail(user.getMail())) {
            throw new Exception("Email Has been occupied");
        }
        //usermail
        if (validateUserNick(user.getNick())) {
            throw new Exception("Nick Has been occupied");
        }
        PassDisposer.encryptPassword(user);
        user.setId(null);
        user.setLocked(false);

        int i = userDao.insertByRow(user);

        Assert.isTrue(i == 1, "user insert failed");
        if (user.getId() != null) {
            if (!Boolean.parseBoolean(comVarService.getComVarValueByName("SimpleRegister"))) {
                basicinfo.setId(null);
                basicinfo.setUserId(user.getId());
                userBasicinfoDao.insertByRow(basicinfo);
            } else {
                UserBasicinfo newBasicInfo = new UserBasicinfo();
                newBasicInfo.setUserId(user.getId());
                userBasicinfoDao.insertByRow(newBasicInfo);
            }
        }

        return user;
    }
}
