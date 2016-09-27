package com.silentgo.core.plugin.db.generate;

import com.silentgo.core.plugin.db.Table;
import com.silentgo.core.plugin.db.TableModel;
import com.silentgo.utils.StringKit;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.util.*;

/**
 * Project : parent
 * Package : com.silentgo.core.plugin.db.generate
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/27.
 */
public class Generate {

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
                String columnType = colRet.getString("TYPE_NAME");
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
            ResultSet colRs = stm.executeQuery("select * from " + tableName);
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

    private String getModelString(TableMeta table, String packageName) {
        StringBuilder builder = new StringBuilder();

        Set<String> imports = new HashSet<>();
        imports.add(TableModel.class.getName());
        imports.add(Table.class.getName());

        StringBuilder body = new StringBuilder();

        for (Column column : table.getColumns()) {
            if (column.getTypeName().equals("Date")) {
                imports.add(java.util.Date.class.getName());
            }
            body.append(getField(column));
            body.append(getGetFunction(column));
            body.append(getSetFunction(column));
        }
        builder.append(format(ClassConst._package, packageName));

        for (String anImport : imports) {
            builder.append(format(ClassConst._importOne, anImport));
        }
        builder.append(ClassConst.newline);
        String an;
        if (table.getPrimaryKeys().size() == 0) {
            an = "(\"" + table.getName() + "\")";
        } else if (table.getPrimaryKeys().size() == 1) {
            an = "(value=\"" + table.getName() +
                    "\",  primaryKey = \"" + table.getPrimaryKeys().get(0) + "\")";
        } else {
            an = "(value=\"" + table.getName() +
                    "\",  primaryKey = {\"" +
                    StringKit.join(table.getPrimaryKeys(), "\",\"") + "\"})";
        }
        builder.append(format(ClassConst._annotaion, Table.class.getSimpleName() + an));

        builder.append(format(ClassConst._classbody, table.getName(), body.toString()));
        return builder.toString();
    }

    private String getField(Column column) {
        return format(ClassConst._field_null,
                column.getTypeName(),
                column.getName());
    }

    private String getSetFunction(Column column) {
        return format(ClassConst._setter,
                StringKit.firstToUpper(column.getName()),
                column.getTypeName(),
                column.getName(), column.getName(), column.getName());
    }

    private String getGetFunction(Column column) {
        return format(ClassConst._getter,
                column.getTypeName(),
                StringKit.firstToUpper(column.getName()),
                column.getName());
    }

    private String format(String string, Object... objects) {
        string = string.replace("$s", "%s");
        return String.format(string, objects);
    }

    public void run(GenerateConfig config) throws SQLException, ClassNotFoundException {
        List<TableMeta> tables = getTables(config);

        StringBuilder builder = new StringBuilder();

        for (TableMeta table : tables) {
            String a = getModelString(table, "com.silentgo.model");
            a = a.replace(ClassConst.newline, "\n").replace(ClassConst.tab, "\t");
            int i = 2;
        }
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        GenerateConfig config = new GenerateConfig();
        config.setBasePath("/s");
        config.setDriver("com.mysql.cj.jdbc.Driver");
        config.setPass("tywhxhxr2012");
        config.setUser("root");
        config.setDbName("lc4e");
        config.setUrl("jdbc:mysql://localhost:3306/lc4e?useUnicode=true&characterEncoding=utf-8&useSSL=true");
        new Generate().run(config);
    }
}
