package com.silentgo.core.config;

import com.silentgo.core.support.BaseFactory;
import com.silentgo.servlet.SilentGoContext;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.PropKit;

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

    public SilentGoConfig(List<String> scanPackages, List<String> scanJars, boolean devMode, String encoding, int contextPathLength, String fileName) {
        setScanPackages(scanPackages);
        setScanJars(scanJars);
        setDevMode(devMode);
        setEncoding(encoding);
        setContextPathLength(contextPathLength);
        setPropKit(new PropKit(fileName, encoding));
        scanJars.add(Const.ApplicationName + "-" + getPropKit().getValue(Const.Version) + ".jar");
        setScanJars(scanJars);
    }

    public boolean addFactory(Class<? extends BaseFactory> factoryClz, BaseFactory baseFactory) {
        return CollectionKit.MapAdd(getFactoryMap(), factoryClz, baseFactory);
    }

    public boolean addFactory(BaseFactory baseFactory) {
        return CollectionKit.MapAdd(getFactoryMap(), baseFactory.getClass(), baseFactory);
    }

    public boolean addFactory(Class<? extends BaseFactory> factoryClz) {
        try {
            return addFactory(factoryClz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T extends BaseFactory> T getFactory(Class<T> name) {
        return (T) getFactoryMap().get(name);
    }

    public boolean addStatic(String name) {
        return CollectionKit.ListAdd(getStaticFolder(), name);
    }

    public boolean setCtx(Request request, Response response) {
        getCtx().set(new SilentGoContext(response, request));
        return true;
    }
}
