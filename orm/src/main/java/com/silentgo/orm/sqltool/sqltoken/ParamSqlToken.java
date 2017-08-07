package com.silentgo.orm.sqltool.sqltoken;

import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqltool.SqlToken;
import com.silentgo.orm.sqltool.SqlTokenResult;
import com.silentgo.utils.StringKit;

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
        return new SqlTokenResult(SQLKit.parameter, object);

    }


}
