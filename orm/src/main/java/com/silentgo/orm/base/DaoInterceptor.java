package com.silentgo.orm.base;

import com.silentgo.orm.SilentGoOrm;
import com.silentgo.orm.connect.ConnectManager;
import com.silentgo.orm.infobuilder.BaseTableBuilder;
import com.silentgo.orm.kit.DaoMethodKit;
import com.silentgo.orm.kit.PropertyTool;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.daoresolve.DaoResolver;
import com.silentgo.orm.sqlparser.daoresolve.DefaultDaoResolver;
import com.silentgo.orm.sqlparser.methodnameparser.MethodParserKit;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.ConvertKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public class DaoInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoInterceptor.class);

    private static final Map<String, Class<? extends BaseDao>> cacheClz = new HashMap<>();
    private static final Map<Method, List<String>> cacheNamePaser = new HashMap<>();

    public static <T> T proxy(Class<T> tclz) {
        Enhancer en = new Enhancer();
        en.setSuperclass(tclz);
        en.setCallback(new DaoInterceptor());
        T tt = (T) en.create();
        return tt;
    }

    private static final DefaultDaoResolver daoResolveFactory = new DefaultDaoResolver();

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        BaseTableBuilder baseTableBuilder = BaseTableBuilder.me();
        SQLTool sqlTool = new SQLTool();
        String methodName = method.getName();
        List<String> parsedString;
        List<Annotation> annotations = baseTableBuilder.getMethodListMap().getOrDefault(method, new ArrayList<>());
        Class<? extends BaseDao> daoClass = (Class<? extends BaseDao>) method.getDeclaringClass();
        if (daoClass.isInterface()) {
            daoClass = getClz(o.getClass().getName());
        }
        BaseTableInfo tableInfo = baseTableBuilder.getClassTableInfoMap().get(daoClass);
        BaseDaoDialect daoDialect = baseTableBuilder.getDialect(tableInfo.getType());
        boolean cached = cacheNamePaser.containsKey(method);
        if (cached) {
            parsedString = cacheNamePaser.get(method);
        } else {
            parsedString = new ArrayList<>();
            MethodParserKit.parse(methodName, annotations, parsedString, tableInfo);
            cacheNamePaser.put(method, parsedString);
        }

        Map<String, Object> namdObjects = new HashMap<>();
        Object[] newObjects = SQLKit.getNamedObject(method, objects, namdObjects);

        Class<?> methodRetType = method.getReturnType();
        boolean[] isHandled = new boolean[]{false};
        Integer[] commonIndex = new Integer[]{0};
        for (DaoResolver daoResolver : daoResolveFactory.getResolverList()) {
            if (daoResolver.handle(methodName, parsedString, annotations)) {
                sqlTool = daoResolver.processSQL(methodName, methodRetType, newObjects, commonIndex,
                        parsedString, tableInfo, sqlTool, annotations, isHandled, daoDialect, namdObjects);
            }
        }

        DBConnect connect = null;
        Object ret = null;

        SQLType type = sqlTool.getType();
        Object[] args = sqlTool.getParams();
        switch (type) {
            case QUERY:
            case COUNT: {
                DaoMethod daoMethod = getDaoMethod(method, daoClass, baseTableBuilder.getReflectMap().get(daoClass));
                connect = ConnectManager.me().getConnect(tableInfo.getType(), tableInfo.getPoolName());
                ret = excuteQuery(sqlTool, args, connect, daoMethod);
                break;
            }
            case DELETE:
            case UPDATE: {
                connect = ConnectManager.me().getConnect(tableInfo.getType(), tableInfo.getPoolName());
                ret = SilentGoOrm.updateOrDelete(connect, sqlTool.getSQL(), type.name(), int.class, args);
                break;
            }
            case INSERT: {
                connect = ConnectManager.me().getConnect(tableInfo.getType(), tableInfo.getPoolName());
                Object[] generateKeys = new Object[objects.length];
                ret = SilentGoOrm.insert(connect, sqlTool.getSQL(), int.class, generateKeys, args);
                resolveInsertResult(tableInfo, generateKeys, objects);
                break;
            }
        }

        if (connect != null && connect.getConnect().getAutoCommit()) {
            connect.release();
        }

        return ret;
    }

    private Class<? extends BaseDao> getClz(String clzString) {
        int index = clzString.indexOf("$$EnhancerByCGLIB");
        String clzStr = clzString.substring(0, index);
        Class<? extends BaseDao> clz = cacheClz.get(clzStr);
        if (clz == null) {
            try {
                clz = (Class<? extends BaseDao>) Class.forName(clzStr);
                cacheClz.put(clzStr, clz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clz;
    }

    private void resolveInsertResult(BaseTableInfo tableInfos, Object[] generateKeys, Object[] params) throws InvocationTargetException, IllegalAccessException {
        if (tableInfos.getPrimaryKeys().size() == 0) return;
        Map<String, PropertyDescriptor> propertyDescriptorMap = PropertyTool.getCachedProps(tableInfos);
        PropertyDescriptor p = propertyDescriptorMap.get(tableInfos.getPrimaryKeys().get(0));
        for (int i = 0; i < generateKeys.length; i++) {
            p.getWriteMethod().invoke(params[i], ConvertKit.getTypeConvert(String.class, p.getPropertyType()).convert(generateKeys[i].toString()));
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
