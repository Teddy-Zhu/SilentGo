package com.silentgo.orm.sqltool.sqltoken;

import com.silentgo.orm.sqltool.SqlToken;
import com.silentgo.orm.sqltool.SqlTokenResult;

public class StringSqlToken implements SqlToken {

    private String sql;

    public StringSqlToken(String sql) {
        this.sql = sql;
    }

    @Override
    public SqlTokenResult handleToken(Object source, Object root) {
        return new SqlTokenResult(sql);
    }

}
