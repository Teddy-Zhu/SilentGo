package com.silentgo.orm.generate;

import com.silentgo.orm.base.BaseDao;
import com.silentgo.utils.StringKit;

/**
 * Project : parent
 * Package : com.silentgo.orm.generate
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/28.
 */
public class TableDaoGenerate {

    public String run(TableMeta tableMeta, String packageName, String modelPackage) {
        StringBuilder builder = new StringBuilder();

        builder.append(format(ClassConst._package, packageName));
        builder.append(format(ClassConst._importOne, BaseDao.class.getName()));
        if (!packageName.equals(modelPackage)) {
            builder.append(format(ClassConst._importOne, modelPackage + "." + tableMeta.getName()));
        }
        builder.append(format(ClassConst._importOne, "com.silentgo.core.ioc.annotation.Service"));
        builder.append(format(ClassConst._annotaion, "Service"));
        builder.append(format(ClassConst._interfaceclassbody_extend_t, StringKit.firstToUpper(tableMeta.getName()) + "Dao",
                BaseDao.class.getSimpleName(), StringKit.firstToUpper(tableMeta.getName()), ""));

        return builder.toString();
    }

    private String format(String string, Object... objects) {
        string = string.replace("$s", "%s");
        return String.format(string, objects);
    }
}
