package com.silentgo.orm.sqltool.condition;

import com.silentgo.orm.sqltool.SqlResult;
import com.silentgo.orm.sqltool.SqlTokenGroup;

public interface SqlCondition {

    public boolean clearSql();

    public SqlResult handleCondition(Object params);

    public boolean appendSql(String sql);

    public boolean appendCondtion(SqlTokenGroup sqlTokenGroup);

}
