package com.silentgo.core.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.plugin.db.generate.TableMeta;
import com.silentgo.core.plugin.db.generate.TableMetaGenerate;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.DBConnect;

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

    private static final String Dot = ".";

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        tableInfoMap = new HashMap<>();
        classTableInfoMap = new HashMap<>();
        reflectMap = new HashMap<>();

        me.getFactory(SqlFactory.class);

        DBType type = DBType.parse(me.getConfig().getDbType());

        if (type == null) return false;

        DBConnect connect = me.getConnect();
        if (connect == null && me.getAnnotationManager().getClasses(Table.class).size() > 0) {
            throw new AppBuildException("can not get connect");
        }
        List<TableMeta> tableMetas = new ArrayList<>();

        try {
            tableMetas = new TableMetaGenerate().getTables(connect.getConnect());
            connect.release();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //build table info
        List<TableMeta> finalTableMetas = tableMetas;
        me.getAnnotationManager().getClasses(Table.class).forEach(tableclass -> {
            Table table = (Table) tableclass.getAnnotation(Table.class);
            if (TableModel.class.isAssignableFrom(tableclass)) {
                BaseTableInfo info = new BaseTableInfo();
                info.setTableName(Const.DEFAULT_NONE.equals(table.value()) ? tableclass.getSimpleName() : table.value());
                info.setClazz(tableclass);
                info.setPrimaryKeys(Arrays.asList(table.primaryKey()));
                info.setType(type);
                Map<String, Class<?>> colClz = new HashMap<>();
                Map<String, String> fullMap = new HashMap<String, String>();

                TableMeta meta = finalTableMetas.stream().filter(t -> t.getTableName().equals(info.getTableName())).findFirst().get();

                fullMap.put("*", info.getTableName() + Dot + "*");
                meta.getColumns().forEach(column -> {
                    try {
                        colClz.put(column.getColName(), Class.forName(column.getTypeString()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    fullMap.put(column.getColName(), info.getTableName() + Dot + column.getColName());
                });

                info.setColumnsMap(colClz);
                info.setFullColumns(fullMap);

                tableInfoMap.put(tableclass, info);
            }
        });
        return false;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
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
