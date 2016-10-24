package com.silentgo.core.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.*;
import com.silentgo.orm.base.annotation.Table;
import com.silentgo.orm.kit.BaseTableInfoKit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

/**
 * Project : parent
 * Package : com.silentgo.core.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/2.
 */
@Factory
public class DaoFactory extends BaseFactory {

    //model - tableInfo
    private Map<Class<? extends TableModel>, BaseTableInfo> tableInfoMap;

    //DAO - tableInfo
    private Map<Class<? extends BaseDao>, BaseTableInfo> classTableInfoMap;

    //Dao - model
    private Map<Class<? extends BaseDao>, Class<? extends TableModel>> reflectMap;

    private Map<Method, List<Annotation>> methodListMap;

    private static final String Dot = ".";

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        tableInfoMap = new HashMap<>();
        classTableInfoMap = new HashMap<>();
        reflectMap = new HashMap<>();
        methodListMap = new HashMap<>();

        me.getFactory(SqlFactory.class);

        DBType type = DBType.parse(me.getConfig().getDbType());

        if (type == null) return false;

        DBConnect connect = me.getConnect();
        if (connect == null && me.getAnnotationManager().getClasses(Table.class).size() > 0) {
            throw new AppBuildException("can not get connect");
        }

        //build table info
        me.getAnnotationManager().getClasses(Table.class).forEach(tableclass -> {
            Table table = (Table) tableclass.getAnnotation(Table.class);
            if (TableModel.class.isAssignableFrom(tableclass)) {
                BaseTableInfo info = null;
                try {
                    info = BaseTableInfoKit.getTableInfo(connect.getConnect(), tableclass, table.value(), Arrays.asList(table.primaryKey()), type);
                    tableInfoMap.put(tableclass, info);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        connect.release();
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }

    public Map<Method, List<Annotation>> getMethodListMap() {
        return methodListMap;
    }

    public Map<Class<? extends TableModel>, BaseTableInfo> getTableInfoMap() {
        return tableInfoMap;
    }

    public Map<Class<? extends BaseDao>, BaseTableInfo> getClassTableInfoMap() {
        return classTableInfoMap;
    }

    public BaseTableInfo getTableInfo(Class<? extends BaseDao> clz) {
        return classTableInfoMap.get(clz);
    }

    public Map<Class<? extends BaseDao>, Class<? extends TableModel>> getReflectMap() {
        return reflectMap;
    }

    public BaseTableInfo getModelTableInfo(Class<? extends TableModel> clz) {
        return tableInfoMap.get(clz);
    }
}
