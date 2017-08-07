package com.silentgo.orm.sqltool;

import java.util.ArrayList;
import java.util.List;

public class SqlResult {


    private StringBuilder sql;
    private List<Object> dy;

    public SqlResult() {
        this.sql = new StringBuilder();
        this.dy = new ArrayList<>();
    }

    public StringBuilder getSql() {
        return sql;
    }

    public List<Object> getDy() {
        return dy;
    }

    public SqlResult addObjects(List<Object> objects) {
        dy.addAll(objects);
        return this;
    }

    public SqlResult addObject(Object object) {
        dy.add(object);
        return this;
    }

    public SqlResult appendSql(String sql) {
        this.sql.append(sql);
        return this;
    }

    public SqlResult append(SqlResult sqlResult) {
        sql.append(sqlResult.getSql());
        dy.addAll(sqlResult.getDy());
        return this;
    }

    public SqlResult append(SqlTokenResult sqlTokenResult) {
        sql.append(sqlTokenResult.getSql());
        dy.addAll(sqlTokenResult.getObjects());
        return this;
    }
}
