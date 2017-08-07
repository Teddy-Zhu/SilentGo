package com.silentgo.orm.sqltool;

public interface SqlToken {

    public SqlTokenResult handleToken(Object source, Object root);

}
