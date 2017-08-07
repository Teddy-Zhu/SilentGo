package com.silentgo.orm.sqltool.sqltoken;

import com.silentgo.orm.sqltool.SqlToken;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.orm.sqltool.SqlTokenResult;
import com.silentgo.utils.StringKit;

public class IfSqlToken implements SqlToken {

    private String[] path;

    private SqlTokenGroup sqlTokenGroup;

    public IfSqlToken(String fullName, String sql) {
        if (StringKit.isBlank(fullName)) throw new IllegalArgumentException();
        this.path = fullName.split("\\.");
        this.sqlTokenGroup = new SqlTokenGroup(sql);
    }

    @Override
    public SqlTokenResult handleToken(Object source, Object root) {

        Object object = source;
        for (String s : path) {
            if (object == null) break;
            object = SqlTokenKit.parseObject(s, object);
        }
        if (object == null) {
            return new SqlTokenResult("");
        } else if (object instanceof Boolean || object.getClass().equals(boolean.class)) {
            if (!(boolean) object) {
                return new SqlTokenResult("");
            }
        }
        return new SqlTokenResult(sqlTokenGroup.getSql(source));
    }
}
