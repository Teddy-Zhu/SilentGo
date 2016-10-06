package com.silentgo.core.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.db.daoresolve.DaoResolveFactory;
import com.silentgo.core.db.daoresolve.DaoResolver;
import com.silentgo.core.db.funcanalyse.AnalyseKit;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;
import com.silentgo.orm.SilentGoOrm;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;
import com.sun.xml.internal.rngom.parse.host.Base;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public class DaoInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLog(DaoInterceptor.class);


    public static <T> T proxy(Class<T> tclz) {
        Enhancer en = new Enhancer();
        en.setSuperclass(tclz);
        en.setCallback(new DaoInterceptor());
        T tt = (T) en.create();
        return tt;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        SilentGo instance = SilentGo.getInstance();
        DaoResolveFactory daoResolveFactory = instance.getFactory(DaoResolveFactory.class);
        DaoFactory daoFactory = instance.getFactory(DaoFactory.class);
        SQLTool sqlTool = new SQLTool();
        String methodName = method.getName();
        List<String> parsedString = new ArrayList<>();
        AnalyseKit.analyse(method.getName(), parsedString);
        Class<? extends BaseDao> daoClass = (Class<? extends BaseDao>) o.getClass().getInterfaces()[0];
        Class<?> returnClass = getTrueReturnType(method.getReturnType(), daoClass, daoFactory);
        boolean[] isHandled = new boolean[]{false};
        for (DaoResolver daoResolver : daoResolveFactory.getResolverList()) {
            if (daoResolver.handle(methodName, parsedString) && !isHandled[0]) {
                try {
                    sqlTool = daoResolver.processSQL(methodName, returnClass, objects, parsedString,
                            daoFactory.getTableInfo(daoClass), sqlTool, isHandled);
                } catch (AppSQLException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("{}", sqlTool);

        return SilentGoOrm.query(instance.getConnect(), sqlTool.getSQL(), returnClass, sqlTool.getParams());
    }

    private Class<?> getTrueReturnType(Class<?> clz, Class<? extends BaseDao> daoClz, DaoFactory daoFactory) {
        if (TableModel.class.equals(clz)) {
            return daoFactory.getReflectMap().get(daoClz);
        }
        return clz;
    }

}
