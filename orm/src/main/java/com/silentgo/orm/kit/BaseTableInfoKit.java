package com.silentgo.orm.kit;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.Column;
import com.silentgo.orm.base.DBType;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.common.Const;
import com.silentgo.orm.generate.TableDaoGenerate;
import com.silentgo.orm.generate.TableMeta;
import com.silentgo.orm.generate.TableMetaGenerate;
import com.silentgo.utils.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/24.
 */
public class BaseTableInfoKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTableInfoKit.class);

    private static final String DOT = ".";

    public static BaseTableInfo getTableInfo(String poolName, Connection connection, Class<? extends TableModel> table, String tableName, List<String> keys, DBType type) throws SQLException, ClassNotFoundException {
        LOGGER.info("get table pool:{},connect:{},table:{},dbtype:{}", poolName, connection == null, tableName, type);
        BaseTableInfo info = new BaseTableInfo();
        info.setTableName(tableName);
        info.setClazz(table);
        info.setPrimaryKeys(keys);
        info.setType(type);
        info.setPoolName(poolName);
        Map<String, Column> fullMap = new HashMap<String, Column>();
        Map<String, Column> originMap = new HashMap<>();
        TableMeta tableMeta = new TableMetaGenerate().getTable(connection, tableName);


        List<String> all = new ArrayList<>();
        for (Field field : table.getDeclaredFields()) {
            com.silentgo.orm.base.annotation.Column an = field.getAnnotation(com.silentgo.orm.base.annotation.Column.class);

            boolean anexist = an != null;
            Column nc = new Column();
            nc.setPropName(field.getName());
            nc.setType(field.getType());
            nc.setColumnName(anexist && !Const.EmptyString.equals(an.value()) ? an.value() : field.getName());
            nc.setNullable(anexist && an.nullable());
            nc.setAutoIncrement(anexist && an.aic());
            nc.setHasDefault(anexist && an.def());
            nc.setFullName(tableMeta.getTableName() + DOT + nc.getColumnName());
            nc.setSelectFullName(nc.getFullName() + (nc.getPropName().equals(nc.getColumnName()) ? "" : " as " + nc.getPropName()));
            fullMap.put(nc.getPropName(), nc);
            originMap.put(nc.getColumnName(), nc);
            all.add(nc.getSelectFullName());
        }

        fullMap.put("*", new Column(tableMeta.getTableName() + DOT + "*", StringKit.join(all, ","), "*"));

//        tableMeta.getColumns().forEach(column -> {
//            Column nc = new Column();
//
//            nc.setPropName(column.getName());
//            try {
//                nc.setType(Class.forName(column.getTypeString()));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            nc.setColumnName(column.getColName());
//            nc.setFullName(tableMeta.getTableName() + DOT + column.getColName());
//            fullMap.put(nc.getPropName(), nc);
//        });

        info.setColumnInfo(fullMap);
        info.setOriginColumn(originMap);
        return info;
    }
}
