package com.silentgo.orm.generate;

import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.base.annotation.Column;
import com.silentgo.orm.base.annotation.Table;
import com.silentgo.utils.StringKit;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Project : parent
 * Package : com.silentgo.orm.generate
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/28.
 */
public class TableModelGenerate {

    public String getModelString(TableMeta table, String packageName) {
        StringBuilder builder = new StringBuilder();

        Set<String> imports = new HashSet<>();
        imports.add(TableModel.class.getName());
        imports.add(Table.class.getName());

        StringBuilder body = new StringBuilder();

        for (TableColumn column : table.getColumns()) {
            if (column.getTypeName().equals("Date")) {
                imports.add(java.util.Date.class.getName());
            } else if (column.getTypeName().equals("BigDecimal")) {
                imports.add(BigDecimal.class.getName());
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
            an = "(\"" + table.getTableName() + "\")";
        } else if (table.getPrimaryKeys().size() == 1) {
            an = "(value=\"" + table.getTableName() +
                    "\",  primaryKey = \"" + table.getPrimaryKeys().get(0) + "\")";
        } else {
            an = "(value=\"" + table.getTableName() +
                    "\",  primaryKey = {\"" +
                    StringKit.join(table.getPrimaryKeys(), "\",\"") + "\"})";
        }

        builder.append(format(ClassConst._annotaion, Table.class.getSimpleName() + an));

        builder.append(format(ClassConst._classbody_extend, StringKit.firstToUpper(table.getName()), TableModel.class.getSimpleName(), body.toString()));
        return builder.toString();
    }

    private String getField(TableColumn column) {
        String field = format(ClassConst._field_null,
                column.getTypeName(),
                column.getName());
        String an = "$t" + format(ClassConst._annotaion, Column.class.getSimpleName() + (column.getName().equals(column.getColName()) ? "" : "(" + column.getColName() + ")"));
        return an + field;
    }

    private String getSetFunction(TableColumn column) {
        return format(ClassConst._setter,
                StringKit.firstToUpper(column.getName()),
                column.getTypeName(),
                column.getName(), column.getName(), column.getName());
    }

    private String getGetFunction(TableColumn column) {
        return format(ClassConst._getter,
                column.getTypeName(),
                StringKit.firstToUpper(column.getName()),
                column.getName());
    }

    private String format(String string, Object... objects) {
        string = string.replace("$s", "%s");
        return String.format(string, objects);
    }

}
