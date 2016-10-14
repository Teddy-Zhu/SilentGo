package com.silentgo.lc4e.web.service;


import com.silentgo.core.cache.annotation.Cache;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.lc4e.config.Key;
import com.silentgo.lc4e.dao.SysJob;
import com.silentgo.lc4e.dao.SysJobDao;

import java.util.List;

/**
 * Created by teddy on 2015/8/11.
 */
@Service
public class JobService {

    @Inject
    SysJobDao sysJobDao;

    @Cache(cacheName = Key.ComVar, key = "allJobs")
    public List<SysJob> getAllJobs() {
        return sysJobDao.queryAll();
    }
}
