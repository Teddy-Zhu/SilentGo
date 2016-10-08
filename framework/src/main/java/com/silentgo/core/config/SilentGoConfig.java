package com.silentgo.core.config;

import com.silentgo.core.SilentGo;
import com.silentgo.core.cache.CacheManager;
import com.silentgo.core.db.DBConfig;
import com.silentgo.core.db.DBType;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.servlet.SilentGoContext;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.PropKit;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/15.
 */
public class SilentGoConfig extends BaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLog(SilentGoConfig.class);


    public SilentGoConfig(List<String> scanPackages, List<String> scanJars, boolean devMode, String encoding, int contextPathLength, String fileName) {
        setScanPackages(scanPackages);
        setScanJars(scanJars);
        setDevMode(devMode);
        setEncoding(encoding);
        setContextPathLength(contextPathLength);
        setInnerPropKit(new PropKit(fileName, encoding));
        scanJars.add(Const.ApplicationName + "-" + getInnerPropKit().getValue(Const.Version) + ".jar");
        setScanJars(scanJars);
        if (StringKit.isNotBlank(getPropfile())) {
            setUserProp(new PropKit(getPropfile(), encoding));
        }
    }

    private boolean addFactory(BaseFactory baseFactory) {
        return CollectionKit.MapAdd(getFactoryMap(), baseFactory.getClass(), baseFactory);
    }

    private boolean addFactory(Class<? extends BaseFactory> factoryClz) {
        try {
            return addFactory(factoryClz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T extends BaseFactory> T getFactory(Class<T> name, SilentGo me) {
        BaseFactory factory = getFactoryMap().get(name);
        if (factory == null) {
            try {
                factory = name.newInstance();
                addFactory(factory);
                factory.initialize(me);
            } catch (InstantiationException | IllegalAccessException | AppBuildException e) {
                e.printStackTrace();
            }
        }
        return (T) factory;
    }

    public <T extends CacheManager> T getCacheManager(Class<T> name) {
        return (T) getCacheManagerMap().get(name);
    }

    public boolean addEndStatic(String name) {
        return CollectionKit.ListAdd(getStaticEndWith(), name);
    }

    public boolean addStartStatic(String name) {
        return CollectionKit.ListAdd(getStaticStartWith(), name);
    }

    public boolean setCtx(Request request, Response response) {
        getCtx().set(new SilentGoContext(response, request));
        return true;
    }

    public DBConnect getConnect(String type, String name) {
        ThreadLocal<DBConnect> connectThreadLocal = getThreadConnect();
        DBConnect connect = connectThreadLocal.get();
        if (connect == null) {
            DBConfig config = (DBConfig) getConfig(type);
            connect = config.getManager().getConnect(name);
            connectThreadLocal.set(connect);
        }
        return connect;
    }

    public DBConnect getConnect(DBType type, String name) {
        ThreadLocal<DBConnect> connectThreadLocal = getThreadConnect();
        DBConnect connect = connectThreadLocal.get();
        if (connect == null) {
            DBConfig config = (DBConfig) getConfig(type);
            connect = config.getManager().getConnect(name);
            connectThreadLocal.set(connect);
        }
        return connect;
    }
}
