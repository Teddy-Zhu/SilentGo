package com.silentgo.test.dao;

import com.silentgo.orm.base.BaseDao;
import com.silentgo.core.ioc.annotation.Service;

@Service
public interface SysMenuDao extends BaseDao<SysMenu> {

    SysMenu queryOneById(String id);
}

