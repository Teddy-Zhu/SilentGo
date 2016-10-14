package com.silentgo.lc4e.dao;

import com.silentgo.core.db.BaseDao;
import com.silentgo.core.ioc.annotation.Service;

@Service
public interface UserDao extends BaseDao<User> {

    public User queryOneByName(String name);

    public Integer countByName(String name);

    public Integer countByNick(String nick);

    public Integer countByMail(String mail);
}

