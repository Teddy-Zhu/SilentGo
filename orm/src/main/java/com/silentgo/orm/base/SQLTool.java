package com.silentgo.orm.base;

import com.silentgo.orm.sqlparser.daoresolve.parameterresolver.DaoParamResolver;
import com.silentgo.orm.sqltool.SqlResult;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.orm.sqltool.condition.CommonSqlCondition;
import com.silentgo.orm.sqltool.condition.ListSqlCondition;
import com.silentgo.orm.sqltool.condition.SqlCondition;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.StringKit;

import java.util.*;

/**
 * Created by teddy on 2015/9/24.
 */
public class SQLTool {

    private static String EmptySplit = " ";
    private String tableName;

    private SQLType type;

    private Map<String, Object> params = new HashMap<>();
    private Object[] objects;

    private SqlCondition insertSqlCondition = new ListSqlCondition("insert into " + this.tableName + " ( ", " ) ", " ", " , ");
    private SqlCondition insertValueSqlCondition = new ListSqlCondition(" values ", " ", " ", " , ");

    private SqlCondition selectSqlCondition = new ListSqlCondition(" select ", "", "", ",");

    private SqlCondition updateSqlCondition = new ListSqlCondition(" ", " ", " ", ",");

    private SqlCondition joinSqlCondition = new ListSqlCondition(" ", " ", " ", " ");

    private SqlCondition whereSqlCondition = new ListSqlCondition(" where ( ", " ) ", " ", " AND ");

    private SqlCondition orderSqlCondition = new ListSqlCondition(" order by ", "", " ", ",");

    private SqlCondition groupSqlCondition = new ListSqlCondition(" group by ", "", " ", ",");

    private SqlCondition limit = new CommonSqlCondition();

    private SqlCondition cached;

    private List<DaoParamResolver> daoParamResolvers = new ArrayList<>();

    public SQLTool() {

    }

    public SQLTool(SQLType sqlType, String sql) {
        this.type = sqlType;
        this.cached = new CommonSqlCondition();
        this.cached.appendSql(sql);
    }

    public SqlResult getSelectSQL() {
        return selectSqlCondition.handleCondition(params);
    }

    public SqlResult getCountRight() {
        SqlResult sqlResult = new SqlResult();
        sqlResult.appendSql(" from ").appendSql(tableName)
                .append(getJoinSQL())
                .append(getWhereSQL())
                .append(getLimit());
        return sqlResult;
    }

    public SqlResult getExceptSQL() {
        SqlResult sqlResult = new SqlResult();
        sqlResult.appendSql(" from ").appendSql(tableName)
                .append(getJoinSQL())
                .append(getWhereSQL())
                .append(getGroupSQL())
                .append(getOrderSQL())
                .append(getLimit());
        return sqlResult;
    }

    public SqlResult getSQL() {
        if (cached == null)
            return getSql(type);
        else return cached.handleCondition(params);
    }

    private SqlResult getSql(SQLType type) {
        if (CollectionKit.isNotEmpty(daoParamResolvers)) {
            for (DaoParamResolver daoParamResolver : daoParamResolvers) {
                daoParamResolver.handleParam(params, objects);
            }
        }
        SqlResult sqlResult = new SqlResult();

        switch (type) {
            case DELETE: {
                sqlResult.append(getDeleteSQL()).append(getWhereSQL());
                break;
            }
            case UPDATE: {
                sqlResult.append(getUpdateSQL()).append(getWhereSQL());
                break;
            }
            case INSERT: {
                sqlResult.append(getInsertSQL());
                break;
            }
            case QUERY: {
                sqlResult.append(getSelectSQL())
                        .append(getExceptSQL());
                break;
            }
            case COUNT: {
                sqlResult.appendSql("select count(1) ")
                        .append(getCountRight());
                break;
            }

        }
        return sqlResult;
    }

    public String getCountSQL() {
        return "select count(1) " + getCountRight();
    }

    private SqlResult getInsertSQL() {

        SqlResult sqlResult = insertSqlCondition.handleCondition(params);

        SqlResult sqlResult2 = insertValueSqlCondition.handleCondition(params);

        return sqlResult.append(sqlResult2);
    }

    public SqlResult getUpdateSQL() {
        SqlResult sqlResult = new SqlResult();
        sqlResult.appendSql("update ").appendSql(tableName)
                .append(getJoinSQL())
                .appendSql(" set ")
                .append(updateSqlCondition.handleCondition(params));
        return sqlResult;
    }

    public SqlResult getDeleteSQL() {
        SqlResult sqlResult = new SqlResult();
        sqlResult.appendSql("delete from ")
                .appendSql(tableName)
                .append(getJoinSQL());
        return sqlResult;
    }

    public SqlResult getWhereSQL() {
        return whereSqlCondition.handleCondition(params);
    }

    public SqlResult getJoinSQL() {
        return joinSqlCondition.handleCondition(params);
    }

    public SqlResult getGroupSQL() {
        return groupSqlCondition.handleCondition(params);
    }

    public SqlResult getOrderSQL() {
        return orderSqlCondition.handleCondition(params);
    }

    public SqlResult getLimit() {
        return limit.handleCondition(params);
    }

    public SQLTool appendParam(String name, Object object) {
        this.params.put(name, object);
        return this;
    }

    public SQLTool updateTableName(String tableName) {
        this.tableName = tableName;
        if (this.insertSqlCondition instanceof ListSqlCondition) {
            ((ListSqlCondition) this.insertSqlCondition).setPrefix("insert into " + tableName + " ( ");
        }
        return this;
    }

    //region update
    public SQLTool update(String tableName) {
        this.type = SQLType.UPDATE;
        updateTableName(tableName);
        return this;
    }

    public SQLTool set(Collection<String> columns) {
        for (String column : columns) {
            updateSqlCondition.appendSql(column);
        }
        return this;
    }

    public SQLTool set(String... columns) {
        for (String column : columns) {
            updateSqlCondition.appendSql(column);
        }
        return this;
    }

    //endregion

    //region select

    public SQLTool select(String tableName, Collection<String> columns) {
        this.type = SQLType.QUERY;
        updateTableName(tableName);

        for (String column : columns) {
            this.selectSqlCondition.appendSql(column);
        }
        return this;
    }

    public SQLTool select(String tableName, String... columns) {
        this.type = SQLType.QUERY;
        updateTableName(tableName);

        for (String column : columns) {
            this.selectSqlCondition.appendSql(column);
        }
        return this;
    }

    public SQLTool selectCol(String... strings) {
        for (String string : strings) {
            this.selectSqlCondition.appendSql(string);
        }
        return this;
    }

    public SQLTool selectCol(Collection<String> collections) {
        for (String collection : collections) {
            this.selectSqlCondition.appendSql(collection);
        }
        return this;
    }
    //endregion select

    //region delete
    public SQLTool delete(String tableName) {
        this.type = SQLType.DELETE;
        updateTableName(tableName);

        return this;
    }

    //endregion
    //region start
    public SQLTool insertValue(SqlTokenGroup sqlTokenGroup) {
        this.insertValueSqlCondition.appendCondtion(sqlTokenGroup);
        return this;
    }

    public SQLTool insertValue(String sql) {
        this.insertValueSqlCondition.appendSql(sql);
        return this;
    }

    public SQLTool insert(String tableName) {
        this.type = SQLType.INSERT;
        updateTableName(tableName);

        return this;
    }

    public SQLTool insert(String tableName, String... columns) {
        this.type = SQLType.INSERT;
        updateTableName(tableName);


        for (String column : columns) {
            this.insertSqlCondition.appendSql(column);
        }
        return this;
    }

    public SQLTool insert(String tableName, Collection<String> columns) {
        this.type = SQLType.INSERT;
        updateTableName(tableName);

        for (String column : columns) {
            this.insertSqlCondition.appendSql(column);
        }
        return this;
    }

    public SQLTool insertCol(String... columns) {
        for (String column : columns) {
            this.insertSqlCondition.appendSql(column);
        }
        return this;
    }
    //endregion

    public SQLTool count(String tableName) {
        this.type = SQLType.COUNT;
        updateTableName(tableName);

        return this;
    }

    public SQLTool from(String tableName) {
        updateTableName(tableName);

        return this;
    }

    public SQLTool set(SqlTokenGroup sqlTokenGroup) {
        this.updateSqlCondition.appendCondtion(sqlTokenGroup);
        return this;
    }

    public SQLTool where(SqlTokenGroup sqlTokenGroup) {
        this.whereSqlCondition.appendCondtion(sqlTokenGroup);
        return this;
    }

    public SQLTool where(String... conditions) {
        for (String condition : conditions) {
            this.whereSqlCondition.appendSql(condition);
        }
        return this;
    }

    private String join(String direct, String tableName) {
        return EmptySplit + direct + " join " + tableName;
    }

    public SQLTool leftJoin(String tableName, String condition) {
        this.joinSqlCondition.appendSql(join("left", tableName) + " on " + condition);
        return this;
    }

    public SQLTool rightJoin(String tableName, String condition) {
        this.joinSqlCondition.appendSql(join("right", tableName) + " on " + condition);
        return this;
    }

    public SQLTool join(String direct, String tableName, String condition) {
        this.joinSqlCondition.appendSql(join(direct, tableName) + " on " + condition);
        return this;
    }

    public SQLTool groupBy(String... columns) {
        for (String column : columns) {
            this.groupSqlCondition.appendSql(column);
        }
        return this;
    }

    public SQLTool orderByDesc(String... columns) {
        for (String column : columns) {
            this.orderSqlCondition.appendSql(orderBy(" DESC ", column));
        }
        return this;
    }

    public SQLTool orderByAsc(String... columns) {
        for (String column : columns) {
            this.orderSqlCondition.appendSql(orderBy(" ASC ", column));
        }
        return this;
    }

    public SQLTool orderBy(String... columns) {
        for (String column : columns) {
            this.orderSqlCondition.appendSql(column);
        }
        return this;
    }

    private String orderBy(String order, String column) {
        return EmptySplit + column + EmptySplit + order;
    }

    public SQLTool orderBy(SqlTokenGroup sqlTokenGroup) {
        this.orderSqlCondition.appendCondtion(sqlTokenGroup);
        return this;
    }

    public SQLTool limit(SqlCondition sqlCondition) {
        this.limit = sqlCondition;
        return this;
    }

    public SQLTool limit(String sql) {
        this.limit.appendSql(" limit " + sql);
        return this;
    }

    public SQLTool limitClear() {
        this.limit.clearSql();
        return this;
    }

    public SQLTool limit(int size, int page) {
        this.limit.appendSql(" limit " + (page - 1) * size + "," + size);
        return this;
    }

    @Override
    public String toString() {
        return "";
    }

    public static String NOTIN(String column, String condition) {
        return column + " NOT IN (" + condition + ")";
    }

    public static String IN(String column, String condition) {
        return column + " IN (" + condition + ")";
    }

    public static String SELECT(String... columns) {
        return " select " + StringKit.join(columns, ",");
    }

    public static String FROM(String tableName) {
        return " from " + tableName;
    }

    public static String WHERE(String logic, String... conditions) {

        return " where ( " + StringKit.join(conditions, " " + logic + " ") + " ) ";
    }

    public static String WHERE(String conditions) {
        return " where ( " + conditions + " ) ";
    }

    public static String OR(String... conditions) {
        return " ( " + StringKit.join(conditions, " OR ") + " ) ";
    }

    public static String AND(String... conditions) {
        return " ( " + StringKit.join(conditions, " AND ") + " ) ";
    }

    public static String COUNT(String column) {
        return " count(" + column + ")";
    }

    public static String AS(String column, String newColumn) {
        return column + " as " + newColumn;
    }

    public SQLType getType() {
        return type;
    }

    public void setType(SQLType type) {
        this.type = type;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public boolean addParamResolver(DaoParamResolver resolver) {
        return daoParamResolvers.add(resolver);
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        updateTableName(tableName);
    }

    public SqlCondition getInsertValueSqlCondition() {
        return insertValueSqlCondition;
    }
}
