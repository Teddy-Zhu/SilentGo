package com.silentgo.orm.sqltool;

import com.silentgo.orm.sqlparser.SQLKit;

import java.util.ArrayList;
import java.util.List;

public class SqlTokenGroup {

    List<SqlToken> sqlTokens;

    public SqlResult getSql(Object object) {
        SqlResult sqlResult = new SqlResult();
        for (SqlToken sqlToken : sqlTokens) {
            sqlResult.append(sqlToken.handleToken(object, object));
        }
        return sqlResult;
    }

    public SqlTokenGroup() {
        this.sqlTokens = new ArrayList<>();
    }

    public SqlTokenGroup(String sql) {
        sqlTokens = new ArrayList<>();
        SQLKit.buildSQL(this, sql);
    }

    public void appendToken(SqlToken sqlToken) {
        this.sqlTokens.add(sqlToken);
    }
}
