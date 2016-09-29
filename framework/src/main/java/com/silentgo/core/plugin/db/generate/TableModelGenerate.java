package com.silentgo.core.plugin.db.generate;

import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;
import com.silentgo.utils.StringKit;

import java.util.HashSet;
import java.util.Set;

/**
 * Project : parent
 * Package : com.silentgo.core.plugin.db.generate
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

        builder.append(format(ClassConst._classbody_extend, table.getName(), TableModel.class.getSimpleName(), body.toString()));
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

}
