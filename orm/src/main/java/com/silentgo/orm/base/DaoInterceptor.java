package com.silentgo.orm.base;

import com.google.common.collect.Maps;
import com.silentgo.orm.SilentGoOrm;
import com.silentgo.orm.connect.ConnectManager;
import com.silentgo.orm.infobuilder.BaseTableBuilder;
import com.silentgo.orm.kit.DaoMethodKit;
import com.silentgo.orm.kit.PropertyKit;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.daoresolve.DaoResolver;
import com.silentgo.orm.sqlparser.daoresolve.DefaultDaoResolver;
import com.silentgo.orm.sqlparser.methodnameparser.MethodParserKit;
import com.silentgo.orm.sqltool.SqlResult;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.ConvertKit;
import com.silentgo.utils.ReflectKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import com.silentgo.utils.reflect.SGClass;
import com.silentgo.utils.reflect.SGMethod;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/9/22.
 */
public class DaoInterceptor implements MethodInterceptor {

    private static final Log LOGGER = LogFactory.get();
    private static final Map<String, Class<? extends BaseDao>> cacheClz = new HashMap<>();
    private static final Map<Method, List<String>> cacheNamePaser = new HashMap<>();

    private static final ArrayList EmptyList = new ArrayList<>();

    public static <T> T proxy(Class<T> tclz) {
        Enhancer en = new Enhancer();
        en.setSuperclass(tclz);
        en.setCallback(new DaoInterceptor());
        T tt = (T) en.create();
        return tt;
    }

    private static final DefaultDaoResolver daoResolveFactory = new DefaultDaoResolver();

    private static final Map<Method, SQLTool> sqlToolCache = new ConcurrentHashMap<>();

    private static final Map<Class<?>, Map<Method, SQLTool>> baseDaoCacheSqlTool = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        Long start = System.currentTimeMillis();
        DBConnect connect = null;

        DaoResolveEntity daoResolveEntity = buildDaoResolveEntity(o, method, objects);

        SQLTool cachedSqlTool = null;
        if (daoResolveEntity.isBaseDao()) {
            Map<Method, SQLTool> sqlToolMapCache = baseDaoCacheSqlTool.get(daoResolveEntity.getDaoClass());
            if (sqlToolMapCache == null) {
                sqlToolMapCache = Maps.newConcurrentMap();
                baseDaoCacheSqlTool.put(daoResolveEntity.getDaoClass(), sqlToolMapCache);
            }
            cachedSqlTool = sqlToolMapCache.get(method);
        } else {
            cachedSqlTool = sqlToolCache.get(method);
        }
        if (cachedSqlTool == null) {
            for (DaoResolver daoResolver : daoResolveFactory.getResolverList()) {
                if (daoResolver.handle(daoResolveEntity)) {
                    daoResolver.processSQL(daoResolveEntity);
                }
            }
            if (daoResolveEntity.isBaseDao()) {
                baseDaoCacheSqlTool.get(daoResolveEntity.getDaoClass()).put(method, daoResolveEntity.getSqlTool());
            } else {
                sqlToolCache.put(method, daoResolveEntity.getSqlTool());
            }
        } else {
            cachedSqlTool.setObjects(daoResolveEntity.getObjects());
            cachedSqlTool.setParams(daoResolveEntity.getNameObjects());
            daoResolveEntity.setSqlTool(cachedSqlTool);
        }

        LOGGER.debug("dao parse sql end in {} ms", System.currentTimeMillis() - start);

        Object ret = null;
        BaseTableBuilder baseTableBuilder = BaseTableBuilder.me();

        BaseTableInfo tableInfo = daoResolveEntity.getTableInfo();

        Class<?> daoClass = daoResolveEntity.getDaoClass();

        try {
            SqlResult sqlResult = daoResolveEntity.getSqlTool().getSQL();

            SQLType type = daoResolveEntity.getSqlTool().getType();
            Object[] args = sqlResult.getDy().toArray();
            String sql = sqlResult.getSql().toString();
            switch (type) {
                case QUERY:
                case COUNT: {
                    DaoMethod daoMethod = getDaoMethod(method, daoClass, baseTableBuilder.getReflectMap().get(daoClass));
                    connect = ConnectManager.me().getConnect(tableInfo.getType(), tableInfo.getPoolName());
                    ret = excuteQuery(sql, args, connect, daoMethod);
                    break;
                }
                case DELETE:
                case UPDATE: {
                    connect = ConnectManager.me().getConnect(tableInfo.getType(), tableInfo.getPoolName());
                    ret = SilentGoOrm.updateOrDelete(connect, sql, type.name(), int.class, args);
                    break;
                }
                case INSERT: {
                    connect = ConnectManager.me().getConnect(tableInfo.getType(), tableInfo.getPoolName());
                    Object[] generateKeys;
                    if (objects.length == 1) {
                        if (objects[0] instanceof Collection)
                            generateKeys = new Object[((Collection) objects[0]).size()];
                        else
                            generateKeys = new Object[1];
                    } else {
                        generateKeys = new Object[0];
                    }
                    ret = SilentGoOrm.insert(connect, sql, int.class, generateKeys, args);
                    resolveInsertResult(tableInfo, generateKeys, objects);
                    break;
                }
            }
            LOGGER.debug("execute sql end");

        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        } finally {
            if (connect != null && connect.getConnect().getAutoCommit()) {
                ConnectManager.me().releaseConnect(tableInfo.getType(), tableInfo.getPoolName(), connect);
            }
            daoResolveEntity.clearSqlTool();
        }

        LOGGER.debug("end orm method : {}", System.currentTimeMillis() - start);
        return ret;
    }


    private DaoResolveEntity buildDaoResolveEntity(Object o, Method method, Object[] objects) {
        Long start = System.currentTimeMillis();

        DaoResolveEntity daoResolveEntity = new DaoResolveEntity();

        BaseTableBuilder baseTableBuilder = BaseTableBuilder.me();

        SGClass sgClass = ReflectKit.getSGClass(method.getDeclaringClass());

        SGMethod sgMethod = sgClass.getMethod(method);

        List<String> parsedString;
        List<Annotation> annotations = baseTableBuilder.getMethodListMap().getOrDefault(method, EmptyList);
        Class<? extends BaseDao> daoClass = (Class<? extends BaseDao>) method.getDeclaringClass();
        if (BaseDao.class.equals(daoClass)) {
            daoResolveEntity.setBaseDao(true);
            daoClass = getClz(o.getClass().getName());
        } else {
            daoResolveEntity.setBaseDao(false);
        }
        BaseTableInfo tableInfo = baseTableBuilder.getClassTableInfoMap().get(daoClass);
        BaseDaoDialect daoDialect = baseTableBuilder.getDialect(tableInfo.getType());
        boolean cached = cacheNamePaser.containsKey(method);
        if (cached) {
            parsedString = cacheNamePaser.get(method);
        } else {
            parsedString = new ArrayList<>();
            MethodParserKit.parse(sgMethod.getName(), annotations, parsedString, tableInfo);
            cacheNamePaser.put(method, parsedString);
        }
        LOGGER.debug("dao prepare for parse object , end in {} ms", System.currentTimeMillis() - start);

        Map<String, Object> namdObjects = new HashMap<>();
        Object[] sequentialObject = SQLKit.parseNamedObject(sgMethod, objects, namdObjects);

        LOGGER.debug("dao parse object end in {} ms", System.currentTimeMillis() - start);

        daoResolveEntity.setHandled(false);
        daoResolveEntity.setSqlTool(new SQLTool());
        daoResolveEntity.setDaoDialect(daoDialect);
        daoResolveEntity.setNameObjects(namdObjects);
        daoResolveEntity.setSgMethod(sgMethod);
        daoResolveEntity.setParsedMethod(parsedString);
        daoResolveEntity.setTableInfo(tableInfo);
        daoResolveEntity.setDaoClass(daoClass);
        daoResolveEntity.setObjects(sequentialObject);
        daoResolveEntity.setObjectIndex(0);
        daoResolveEntity.setObjectParsedIndex(0);


        daoResolveEntity.getSqlTool().setObjects(sequentialObject);
        daoResolveEntity.getSqlTool().setParams(namdObjects);
        return daoResolveEntity;
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
        if (params.length == 1) {
            Map<String, PropertyDescriptor> propertyDescriptorMap = PropertyKit.getCachedProps(tableInfos);
            PropertyDescriptor p = propertyDescriptorMap.get(tableInfos.getPrimaryKeys().get(0));
            if (params[0] instanceof Collection) {
                Collection objects = (Collection) params[0];
                Iterator iterator = objects.iterator();
                int i = 0;
                while (iterator.hasNext() && i < generateKeys.length) {
                    p.getWriteMethod().invoke(iterator.next(), ConvertKit.getTypeConvert(String.class, p.getPropertyType()).convert(generateKeys[i].toString()));
                    i++;
                }
            } else {
                p.getWriteMethod().invoke(params[0], ConvertKit.getTypeConvert(String.class, p.getPropertyType()).convert(generateKeys[0].toString()));
            }
        }
    }

    private Object excuteQuery(String sql, Object[] objects, DBConnect connect, DaoMethod daoMethod) throws SQLException {
        if (daoMethod.isList()) {
            if (daoMethod.isArray()) {
                return SilentGoOrm.queryArrayList(connect, sql, daoMethod.getType(), objects);
            } else {
                return SilentGoOrm.queryList(connect, sql, daoMethod.getType(), objects);
            }
        } else if (daoMethod.isArray()) {
            return SilentGoOrm.queryArray(connect, sql, daoMethod.getType(), objects);
        } else {
            return SilentGoOrm.query(connect, sql, daoMethod.getType(), objects);
        }
    }

    private DaoMethod getDaoMethod(Method method, Class<?> daoClass, Class<? extends TableModel> modelClass) {
        DaoMethod daoMethod = BaseDao.class.equals(method.getDeclaringClass()) ? null : DaoMethodKit.getDaoMethod(method);
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
                if (int.class.equals(method.getReturnType())) {
                    daoMethod.setType(int.class);
                } else {
                    daoMethod.setType(modelClass);
                }
                if (Collection.class.isAssignableFrom(method.getReturnType())) {
                    daoMethod.setList(true);
                }
                return daoMethod;
            }
        }
        return daoMethod;
    }


}
