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

    private static final String DOT = ".";

    public static BaseTableInfo getTableInfo(Connection connection, Class<? extends TableModel> table, String tableName, List<String> keys, DBType type) throws SQLException, ClassNotFoundException {
        BaseTableInfo info = new BaseTableInfo();
        info.setTableName(tableName);
        info.setClazz(table);
        info.setPrimaryKeys(keys);
        info.setType(type);
        Map<String, Column> fullMap = new HashMap<String, Column>();
        TableMeta tableMeta = new TableMetaGenerate().getTable(connection, tableName);


        List<String> all = new ArrayList<>();
        for (Field field : table.getDeclaredFields()) {
            com.silentgo.orm.base.annotation.Column an = field.getAnnotation(com.silentgo.orm.base.annotation.Column.class);

            Column nc = new Column();

            nc.setPropName(field.getName());
            nc.setType(field.getType());
            nc.setColumnName(an != null && !Const.EmptyString.equals(an.value()) ? an.value() : field.getName());
            nc.setFullName(tableMeta.getTableName() + DOT + nc.getColumnName());
            fullMap.put(nc.getPropName(), nc);
            all.add(nc.getSelectFullName());
        }

        fullMap.put("*", new Column(tableMeta.getTableName() + DOT + "*", StringKit.join(all, ",")));

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

        return info;
    }
}
