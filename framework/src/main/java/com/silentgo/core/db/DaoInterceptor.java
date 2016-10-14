package com.silentgo.core.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.db.daoresolve.DaoResolveFactory;
import com.silentgo.core.db.daoresolve.DaoResolver;
import com.silentgo.core.db.funcanalyse.AnalyseKit;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;
import com.silentgo.core.plugin.db.util.PropertyTool;
import com.silentgo.orm.SilentGoOrm;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        BaseTableInfo tableInfo = daoFactory.getTableInfo(daoClass);

        Class<?> methodRetType = method.getReturnType();
        boolean[] isHandled = new boolean[]{false};
        List<Annotation> annotations = daoFactory.getMethodListMap().get(method);
        for (DaoResolver daoResolver : daoResolveFactory.getResolverList()) {
            if (daoResolver.handle(methodName, parsedString, annotations) && !isHandled[0]) {
                try {
                    sqlTool = daoResolver.processSQL(methodName, methodRetType, objects, parsedString,
                            tableInfo, sqlTool, annotations, isHandled);
                } catch (AppSQLException e) {
                    e.printStackTrace();
                }
            }
        }
        DBConnect connect = null;
        Object ret = null;
        switch (sqlTool.getType()) {
            case QUERY:
            case COUNT: {
                DaoMethod daoMethod = getDaoMethod(method, daoClass, daoFactory.getReflectMap().get(daoClass));
                connect = instance.getConnect();
                ret = excuteQuery(sqlTool, objects, connect, daoMethod);
                break;
            }
            case DELETE:
            case UPDATE: {
                connect = instance.getConnect();
                ret = SilentGoOrm.updateOrDelete(connect, sqlTool.getSQL(), sqlTool.getType().name(), int.class, objects);
                break;
            }
            case INSERT: {
                connect = instance.getConnect();
                Object[] generateKeys = new Object[objects.length];
                ret = SilentGoOrm.insert(connect, sqlTool.getSQL(), int.class, generateKeys, objects);
                resolveInsertResult(tableInfo, generateKeys, objects);
                break;
            }
        }

        if (connect != null && connect.getConnect().getAutoCommit()) {
            connect.release();
        }
        return ret;
    }

    private void resolveInsertResult(BaseTableInfo tableInfos, Object[] generateKeys, Object[] params) throws InvocationTargetException, IllegalAccessException {
        if (tableInfos.getPrimaryKeys().size() == 0) return;
        Map<String, PropertyDescriptor> propertyDescriptorMap = PropertyTool.getCachedProps(tableInfos);
        PropertyDescriptor p = propertyDescriptorMap.get(tableInfos.getPrimaryKeys().get(0));
        for (int i = 0; i < generateKeys.length; i++) {
            p.getWriteMethod().invoke(params[i], generateKeys[i]);
        }
    }

    private Object excuteQuery(SQLTool sqlTool, Object[] objects, DBConnect connect, DaoMethod daoMethod) throws SQLException {
        if (daoMethod.isList()) {
            if (daoMethod.isArray()) {
                return SilentGoOrm.queryArrayList(connect, sqlTool.getSQL(), daoMethod.getType(), objects);
            } else {
                return SilentGoOrm.queryList(connect, sqlTool.getSQL(), daoMethod.getType(), objects);
            }
        } else if (daoMethod.isArray()) {
            return SilentGoOrm.queryArray(connect, sqlTool.getSQL(), daoMethod.getType(), objects);
        } else {
            return SilentGoOrm.query(connect, sqlTool.getSQL(), daoMethod.getType(), objects);
        }
    }

    private DaoMethod getDaoMethod(Method method, Class<?> daoClass, Class<? extends TableModel> modelClass) {
        DaoMethod daoMethod = DaoMethodKit.getDaoMethod(method);
        if (daoMethod == null) {
            daoMethod = new DaoMethod();
            DaoMethodKit.addDaoMethod(method, daoMethod);
            if (method.getDeclaringClass().equals(daoClass)) {
                if (Collection.class.isAssignableFrom(method.getReturnType())) {
                    daoMethod.setList(true);
                    Class<?> retClz = ClassKit.getActualType(method.getGenericReturnType());
                    if (retClz.isArray()) daoMethod.setArray(true);
                    daoMethod.setType(retClz);
                    return daoMethod;
                } else if (method.getReturnType().isArray()) {
                    daoMethod.setArray(true);
                    daoMethod.setType(method.getReturnType().getComponentType());
                    return daoMethod;
                } else {
                    daoMethod.setType(method.getReturnType());
                    return daoMethod;
                }
            } else {
                daoMethod.setType(modelClass);
                if (Collection.class.isAssignableFrom(method.getReturnType())) {
                    daoMethod.setList(true);
                }
                return daoMethod;
            }
        }
        return daoMethod;
    }

}
