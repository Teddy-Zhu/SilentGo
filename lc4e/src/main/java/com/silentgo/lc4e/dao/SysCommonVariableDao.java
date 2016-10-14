package com.silentgo.lc4e.dao;

import com.silentgo.core.db.BaseDao;
import com.silentgo.core.ioc.annotation.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface SysCommonVariableDao extends BaseDao<SysCommonVariable> {


    public SysCommonVariable queryOneByName(String name);

    public List<SysCommonVariable> queryListByListName(Collection<String> name);

}

