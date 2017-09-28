package com.silentgo.orm.sqltool.condition;

import com.silentgo.orm.sqltool.SqlResult;
import com.silentgo.orm.sqltool.SqlTokenGroup;

public class CommonSqlCondition implements SqlCondition {
    private SqlTokenGroup sqlTokenGroup;

    public CommonSqlCondition() {
    }

    public CommonSqlCondition(String sql) {
        this.sqlTokenGroup = new SqlTokenGroup(sql);
    }

    public CommonSqlCondition(SqlTokenGroup sqlTokenGroup) {
        this.sqlTokenGroup = sqlTokenGroup;
    }

    @Override
    public boolean clearSql() {
        this.sqlTokenGroup = null;
        return true;
    }

    @Override
    public SqlResult handleCondition(Object params) {
        if (sqlTokenGroup == null)
            return new SqlResult();
        return sqlTokenGroup.getSql(params);
    }

    @Override
    public boolean appendSql(String sql) {
        this.sqlTokenGroup = new SqlTokenGroup(sql);
        return true;
    }

    @Override
    public boolean appendCondtion(SqlTokenGroup sqlTokenGroup) {
        this.sqlTokenGroup = sqlTokenGroup;
        return true;
    }


}
