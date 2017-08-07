package com.silentgo.orm.sqltool.sqltoken;

import com.silentgo.orm.sqltool.SqlToken;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.orm.sqltool.SqlTokenResult;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

public class ForEachSqlToken implements SqlToken {

    private static final Log LOGGER = LogFactory.get();

    private String[] namePath;

    private SqlTokenGroup sqlTokenGroup;

    private String prefix;

    private String suffix;

    private String split;

    public ForEachSqlToken(String name, SqlTokenGroup sqlTokenGroup, String prefix, String suffix, String split) {
        if (StringKit.isBlank(name)) throw new IllegalArgumentException();
        this.namePath = name.split("\\.");
        this.sqlTokenGroup = sqlTokenGroup;
        this.prefix = prefix;
        this.suffix = suffix;
        this.split = split;
    }

    public ForEachSqlToken(String sql) {
        this.sqlTokenGroup = new SqlTokenGroup(sql);
    }

    @Override
    public SqlTokenResult handleToken(Object source, Object root) {
        if (source == null) {
            return new SqlTokenResult("");
        }

        Object object = source;
        for (String s : namePath) {
            if (object == null) break;
            object = SqlTokenKit.parseObject(s, object);
        }

        SqlTokenResult sqlTokenResult = new SqlTokenResult("");

        sqlTokenResult.appendSql(prefix);

        if (source instanceof Collection) {

            Iterator iterator = ((Collection) source).iterator();

            Object first = iterator.hasNext() ? iterator.next() : null;


            sqlTokenResult.append(sqlTokenGroup.getSql(first));

            while (iterator.hasNext()) {
                sqlTokenResult.appendSql(split);
                sqlTokenResult.append(sqlTokenGroup.getSql(iterator.next()));
            }
        } else if (source.getClass().isArray()) {
            int length = Array.getLength(source);

            for (int i = 0; i < length - 1; i++) {
                Object now = Array.get(source, i);
                sqlTokenResult.append(sqlTokenGroup.getSql(now));
                sqlTokenResult.appendSql(split);
            }
            sqlTokenResult.append(sqlTokenGroup.getSql(Array.get(source, length - 1)));
        } else {
            LOGGER.debug("the param type is {},must be collection or array", source.getClass().getName());
        }
        sqlTokenResult.appendSql(suffix);

        return sqlTokenResult;
    }
}
