package com.silentgo.orm.sqltool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlTokenResult {

    private StringBuilder sql;
    private List<Object> objects;

    public SqlTokenResult(SqlResult sqlResult) {
        this.sql = sqlResult.getSql();
        this.objects = sqlResult.getDy();
    }

    public SqlTokenResult(String sql) {
        this.sql = new StringBuilder(sql);
        this.objects = new ArrayList<>();
    }

    public SqlTokenResult(String sql, List<Object> objects) {
        this.sql = new StringBuilder(sql);
        this.objects = objects;
    }

    public SqlTokenResult(String sql, Object... objects) {
        this.sql = new StringBuilder(sql);
        this.objects = Arrays.asList(objects);
    }

    public void append(SqlResult sqlResult) {
        this.sql.append(sqlResult.getSql());
        this.objects.addAll(sqlResult.getDy());
    }

    public void appendSql(String sql) {
        this.sql.append(sql);
    }

    public StringBuilder getSql() {
        return sql;
    }

    public void setSql(StringBuilder sql) {
        this.sql = sql;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void appendObject(Object obj) {
        this.objects.add(obj);
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }
}
