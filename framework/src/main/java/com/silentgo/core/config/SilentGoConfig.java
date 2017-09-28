package com.silentgo.core.config;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.cache.CacheManager;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.ioc.bean.Bean;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBType;
import com.silentgo.orm.connect.ConnectManager;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.PropKit;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

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


    private static final Log LOGGER = LogFactory.get();

    public SilentGoConfig(List<String> scanPackages, List<String> scanJars, boolean devMode, String encoding, int contextPathLength, String fileName) {
        setScanPackages(scanPackages);
        setScanJars(scanJars);
        setDevMode(devMode);
        setEncoding(encoding);
        setContextPathLength(contextPathLength);
        setInnerPropKit(new PropKit(fileName, encoding));
        scanJars.add(Const.ApplicationName + "-" + getInnerPropKit().getValue(Const.Version) + ".jar");
        setScanJars(scanJars);
        LOGGER.info("init silentgoconfig ,jar name :{}", scanJars.get(0));
        if (StringKit.isNotBlank(getPropfile()) && getUserProp() != null) {
            setUserProp(new PropKit(getPropfile(), encoding));
        }
    }

    public boolean addFactory(BaseFactory baseFactory) {
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
                getFactory(getBeanClass(), me).addBean(factory, true, false, false);
            } catch (InstantiationException | IllegalAccessException | AppBuildException e) {
                e.printStackTrace();
            }
        }
        return (T) factory;
    }

    public CacheManager getCacheManager() {
        return getCacheManagerMap().get(getCacheClz());
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

    public boolean addStaticMapping(String prefix, String replacement) {
        CollectionKit.MapAdd(getStaticMapping(), prefix, replacement);
        return true;
    }

    public void releaseConnect(String name, DBConnect connect) {
        ConnectManager.me().releaseConnect(DBType.parse(getDbType()), name, connect);
    }

    public DBConnect getConnect(String name) {
        return ConnectManager.me().getConnect(DBType.parse(getDbType()), name);
    }

    public boolean hasConnect(String name) {
        return ConnectManager.me().getThreadConnect(DBType.parse(getDbType()), name) != null;
    }

    public boolean addExtraFactory(Class<? extends BaseFactory> clz) {
        return CollectionKit.ListAdd(getFactories(), clz);
    }

    public Bean getBean(Class<?> name) {
        return getBean(name.getName());
    }

    public Bean getBean(String name) {
        return getFactory(getBeanClass(), SilentGo.me()).getBean(name);
    }

    public boolean addExtraAction(ActionChain chain) {
        return CollectionKit.ListAdd(getActionChains(), chain);
    }

    public boolean addExtraAnInterceptor(Class<? extends IAnnotation> annotation) {
        return CollectionKit.ListAdd(getAnnotationIntecepters(), annotation);
    }

    public boolean addExtraInitConfig(Config config) {
        return CollectionKit.ListAdd(getExtraConfig(), config);
    }
}
