package com.silentgo.core.plugin.db.generate;

import com.silentgo.utils.StringKit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.plugin.db.generate
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/28.
 */
public class TableMetaGenerate implements TableMetaGenerator {

    public List<TableMeta> getTables(GenerateConfig config) throws SQLException, ClassNotFoundException {
        try {
            Class.forName(config.getDriver());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Connection connection = null;

        connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPass());

        DatabaseMetaData dbMetData = connection.getMetaData();

        List<TableMeta> tables = new ArrayList<>();

        ResultSet rs = dbMetData.getTables(connection.getCatalog(), "%", "", new String[]{"TABLE"});

        while (rs.next()) {
            TableMeta tableMeta = new TableMeta();
            String tableName = rs.getString(3).toLowerCase();
            tableMeta.setName(format(tableName));
            ResultSet colRet = dbMetData.getColumns(connection.getCatalog(), "%", tableName,
                    "%");
            ResultSet primaryKeyResultSet = dbMetData.getPrimaryKeys(connection.getCatalog(), null, tableName);

            tableMeta.setPrimaryKeys(new ArrayList<>());
            while (primaryKeyResultSet.next()) {
                tableMeta.getPrimaryKeys().add(primaryKeyResultSet.getString("COLUMN_NAME"));
            }
            tableMeta.setColumns(new ArrayList<>());
            while (colRet.next()) {
                String columnName = colRet.getString("COLUMN_NAME");
                String remarks = colRet.getString("REMARKS");
                int nullable = colRet.getInt("NULLABLE");
                Column column = new Column();
                column.setName(format(columnName));
                //column.setType(Class.forName(TypeMapping.getType(columnType)));
                //column.setTypeString(TypeMapping.getType(columnType));
                column.setNullAble(nullable == 1);
                column.setDescription(remarks);
                tableMeta.getColumns().add(column);
            }
            Statement stm = connection.createStatement();
            ResultSet colRs = stm.executeQuery(getSelectSql(tableName));
            ResultSetMetaData rsmd = colRs.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String name = format(rsmd.getColumnName(i));

                Column column = tableMeta.getColumns().stream()
                        .filter(col -> name.equals(col.getName())).findFirst().get();

                String colClassName = rsmd.getColumnClassName(i);
                String typeStr = TypeMapping.getType(colClassName);

                if (typeStr != null) {
                    column.setTypeString(typeStr);
                } else {
                    int type = rsmd.getColumnType(i);
                    if (type == Types.BINARY || type == Types.VARBINARY || type == Types.BLOB) {
                        column.setTypeString("byte[]");
                    } else if (type == Types.CLOB || type == Types.NCLOB) {
                        column.setTypeString("java.lang.String");
                    } else {
                        column.setTypeString("java.lang.String");
                    }
                }

            }
            primaryKeyResultSet.close();
            colRs.close();
            colRet.close();
            tables.add(tableMeta);
        }
        rs.close();
        connection.close();
        return tables;
    }

    private String format(String text) {
        String[] strings = text.split("_");
        strings[0] = StringKit.firstToLower(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            strings[i] = StringKit.firstToUpper(strings[i]);
        }
        return StringKit.join(strings, "");
    }

    @Override
    public String getSelectSql(String tableName) {
        return "select * from " + tableName;
    }
}
