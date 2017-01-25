package com.silentgo.orm.infobuilder;

import com.silentgo.orm.base.*;
import com.silentgo.orm.base.annotation.Table;
import com.silentgo.orm.connect.ConnectManager;
import com.silentgo.orm.dialect.DialectManager;
import com.silentgo.orm.kit.BaseTableInfoKit;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.infobuilder
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/29.
 */
public class BaseTableBuilder {

    private static final Log LOGGER = LogFactory.get();

    //model - tableInfo
    private Map<Class<? extends TableModel>, BaseTableInfo> tableInfoMap = new HashMap<>();

    //DAO - tableInfo
    private Map<Class<? extends BaseDao>, BaseTableInfo> classTableInfoMap = new HashMap<>();

    //Dao - model
    private Map<Class<? extends BaseDao>, Class<? extends TableModel>> reflectMap = new HashMap<>();

    private Map<Class<? extends TableModel>, Class<? extends BaseDao>> reflectFMap = new HashMap<>();


    private Map<Method, List<Annotation>> methodListMap = new HashMap<>();


    private static class BaseTableHolder {
        static BaseTableBuilder instance = new BaseTableBuilder();
    }

    public static BaseTableBuilder me() {
        return BaseTableHolder.instance;
    }


    public Object getDao(Class<? extends BaseDao> daoClz) {
        return DaoInterceptor.proxy(daoClz);
    }

    public Object initialDao(Class<? extends BaseDao> daoClz) {
        Class<? extends TableModel> tableClass = (Class<? extends TableModel>) ClassKit.getGenericClass(daoClz)[0];

        BaseTableInfo baseTableInfo = tableInfoMap.get(tableClass);
        if (baseTableInfo != null) {
            classTableInfoMap.put(daoClz, baseTableInfo);
            reflectMap.put(daoClz, tableClass);
            reflectFMap.put(tableClass, daoClz);
        }

        for (Method method : daoClz.getDeclaredMethods()) {
            Annotation[] ans = method.getAnnotations();
            BaseTableBuilder.me().getMethodListMap().put(method, Arrays.asList(ans));
        }
        return getDao(daoClz);
    }

    public Object initialBaseModel(String name, Class<? extends TableModel> tableclass, DBType type) {
        if (type == null) return null;

        LOGGER.info(" build base model :{}", name);
        DBConnect connect = ConnectManager.me().getConnect(type, name);

        //build table info
        Table table = (Table) tableclass.getAnnotation(Table.class);

        if (table == null) return null;
        BaseTableInfo info = null;
        try {
            info = BaseTableInfoKit.getTableInfo(name, connect.getConnect(), tableclass, table.value(), Arrays.asList(table.primaryKey()), type);
            tableInfoMap.put(tableclass, info);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error(e);
        } finally {
            ConnectManager.me().releaseConnect(type, name, connect);
        }
        return null;
    }

    public Map<Class<? extends TableModel>, BaseTableInfo> getTableInfoMap() {
        return tableInfoMap;
    }

    public Map<Class<? extends BaseDao>, BaseTableInfo> getClassTableInfoMap() {
        return classTableInfoMap;
    }

    public Map<Class<? extends BaseDao>, Class<? extends TableModel>> getReflectMap() {
        return reflectMap;
    }

    public Map<Class<? extends TableModel>, Class<? extends BaseDao>> getReflectFMap() {
        return reflectFMap;
    }

    public Map<Method, List<Annotation>> getMethodListMap() {
        return methodListMap;
    }

    public BaseDaoDialect getDialect(DBType type) {
        return DialectManager.getDialect(type);
    }
}
