package com.silentgo.orm.sqltool.sqltoken;

import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqltool.SqlToken;
import com.silentgo.orm.sqltool.SqlTokenResult;
import com.silentgo.utils.StringKit;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ParamSqlToken implements SqlToken {


    private String[] path;

    public ParamSqlToken(String fullName) {
        if (StringKit.isBlank(fullName)) throw new IllegalArgumentException();
        path = fullName.split("\\.");
    }

    @Override
    public SqlTokenResult handleToken(Object source, Object root) {

        //SqlTokenResult sqlTokenResult = new SqlTokenResult();

        Object object = source;
        for (String s : path) {
            if (object == null) break;
            object = SqlTokenKit.parseObject(s, object);
        }

        if (object == null) {
            return new SqlTokenResult(SQLKit.parameter, object);
        } else if (object instanceof Collection) {
            SqlTokenResult sqlTokenResult = new SqlTokenResult("");
            Iterator iterator = ((Collection) object).iterator();

            Object first = iterator.hasNext() ? iterator.next() : null;

            sqlTokenResult.appendObject(first);
            sqlTokenResult.appendSql(SQLKit.parameter);

            while (iterator.hasNext()) {
                sqlTokenResult.appendSql("," + SQLKit.parameter);
                sqlTokenResult.appendObject(iterator.next());
            }
            return sqlTokenResult;
        } else if (object.getClass().isArray()) {
            SqlTokenResult sqlTokenResult = new SqlTokenResult("");

            int length = Array.getLength(object);

            for (int i = 0; i < length - 1; i++) {
                Object now = Array.get(object, i);
                sqlTokenResult.appendObject(now);
                sqlTokenResult.appendSql(SQLKit.parameter + ",");
            }

            sqlTokenResult.appendObject(Array.get(object, length - 1));
            sqlTokenResult.appendSql(SQLKit.parameter);
            return sqlTokenResult;
        }

        return new SqlTokenResult(SQLKit.parameter, object);

    }


}
