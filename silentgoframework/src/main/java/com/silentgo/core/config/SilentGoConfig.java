package com.silentgo.core.config;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptor;
import com.silentgo.core.aop.validator.support.ValidatorInterceptor;
import com.silentgo.core.build.SilentGoBuilder;
import com.silentgo.core.build.SilentGoReleaser;
import com.silentgo.core.render.Render;
import com.silentgo.core.render.support.JspRender;
import com.silentgo.core.route.RoutePaser;
import com.silentgo.core.route.support.routeparse.DefaultRouteParser;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.PropKit;
import com.silentgo.kit.SilentGoContext;
import com.silentgo.kit.json.FastJsonPaser;
import com.silentgo.kit.json.JsonPaser;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.sun.xml.internal.rngom.parse.host.Base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/15.
 */
public class SilentGoConfig extends FileUploadConfig {

    public SilentGoConfig(List<String> scanPackages, List<String> scanJars, boolean devMode, String encoding, int contextPathLength, String fileName) {
        setScanPackages(scanPackages);
        setScanJars(scanJars);
        setDevMode(devMode);
        setEncoding(encoding);
        setContextPathLength(contextPathLength);
        setPropKit(new PropKit(fileName, encoding));
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
