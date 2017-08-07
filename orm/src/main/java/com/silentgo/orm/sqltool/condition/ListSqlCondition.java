package com.silentgo.orm.sqltool.condition;

import com.silentgo.orm.sqltool.SqlResult;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.StringKit;

import java.util.ArrayList;
import java.util.List;

public class ListSqlCondition implements SqlCondition {
    private List<SqlTokenGroup> sqlTokens;

    private String prefix;

    private String suffix;

    private String empty;

    private String split;

    public ListSqlCondition(String prefix, String suffix, String empty, String split) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.empty = empty;
        this.split = split;
        this.sqlTokens = new ArrayList<>();
    }


    public ListSqlCondition(List<String> sql, String prefix, String suffix, String empty, String split) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.empty = empty;
        this.split = split;
        this.sqlTokens = new ArrayList<>();

        for (String s : sql) {
            sqlTokens.add(new SqlTokenGroup(s));
        }
    }

    @Override
    public boolean clearSql() {
        sqlTokens = new ArrayList<>();
        return true;
    }

    @Override
    public SqlResult handleCondition(Object params) {
        SqlResult sqlResult = new SqlResult();
        List<StringBuilder> list = new ArrayList<>();
        for (SqlTokenGroup sqlToken : sqlTokens) {
            SqlResult sqlGroupResult = sqlToken.getSql(params);
            sqlResult.addObjects(sqlGroupResult.getDy());
            StringBuilder sb = sqlGroupResult.getSql();
            if (StringKit.isNotBlank(sb.toString())) {
                list.add(sqlGroupResult.getSql());
            }
        }
        if (CollectionKit.isEmpty(list)) {
            sqlResult.appendSql(empty);
        } else {
            sqlResult.appendSql(prefix)
                    .appendSql(StringKit.join(list, split))
                    .appendSql(suffix);
        }
        return sqlResult;
    }

    @Override
    public boolean appendSql(String sql) {
        return this.sqlTokens.add(new SqlTokenGroup(sql));
    }

    @Override
    public boolean appendCondtion(SqlTokenGroup sqlTokenGroup) {
        return this.sqlTokens.add(sqlTokenGroup);
    }
}
