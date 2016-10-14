package com.silentgo.lc4e.web.service;

import com.silentgo.core.cache.annotation.Cache;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.lc4e.config.Key;
import com.silentgo.lc4e.dao.SysCommonVariable;
import com.silentgo.lc4e.dao.SysCommonVariableDao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by teddy on 2015/7/29.
 */
@Service
public class ComVarService {

    @Inject
    SysCommonVariableDao sysCommonVariableDao;

    @Cache(cacheName = Key.ComVar, index = 0)
    public SysCommonVariable getComVarByName(String name) {

        return sysCommonVariableDao.queryOneByName(name);
    }


    @Cache(cacheName = Key.ComVar, index = 0)
    public List<SysCommonVariable> getComVarsByNames(String[] name) {
        return sysCommonVariableDao.queryListByListName(Arrays.asList(name));
    }

    @Cache(cacheName = Key.ComVar, index = 0)
    public List<SysCommonVariable> getComVarsByNames(Collection<String> name) {
        return sysCommonVariableDao.queryListByListName(name);
    }

    @Cache(cacheName = Key.ComVar, index = 0)
    public String getComVarValueByName(String name) {
        SysCommonVariable commonVariable = getComVarByName(name);
        if (commonVariable != null) {
            return commonVariable.getValue();
        } else {
            return null;
        }
    }
}
