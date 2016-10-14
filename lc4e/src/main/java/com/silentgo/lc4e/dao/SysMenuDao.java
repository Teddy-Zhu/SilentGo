package com.silentgo.lc4e.dao;

import com.silentgo.core.db.BaseDao;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.lc4e.entity.MenuEntity;

import java.util.List;

@Service
public interface SysMenuDao extends BaseDao<SysMenu> {

    public List<MenuEntity> queryListOrderByParentIdAndOrder();
}

