package com.silentgo.orm.sqltool.sqltoken;

import com.silentgo.orm.sqltool.SqlToken;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.orm.sqltool.SqlTokenResult;
import com.silentgo.utils.StringKit;

import java.util.Collection;

public class ListContainIfSqlToken implements SqlToken {

    private String[] path;

    private String name;

    private SqlTokenGroup sqlTokenGroup;

    public ListContainIfSqlToken(String fullName, String name, String sql) {
        if (StringKit.isBlank(fullName)) throw new IllegalArgumentException();
        this.path = fullName.split("\\.");
        this.name = name;
        this.sqlTokenGroup = new SqlTokenGroup(sql);
    }

    @Override
    public SqlTokenResult handleToken(Object source, Object root) {

        Object object = source;
        for (String s : path) {
            if (object == null) break;
            object = SqlTokenKit.parseObject(s, object);
        }
        if (!(object instanceof Collection)) throw new IllegalArgumentException(" object type must be collection");

        if (((Collection) object).contains(name)) {
            return new SqlTokenResult(sqlTokenGroup.getSql(source));
        } else {
            return new SqlTokenResult("");
        }
    }
}
