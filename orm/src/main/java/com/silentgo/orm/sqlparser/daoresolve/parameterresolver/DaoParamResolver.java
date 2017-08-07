package com.silentgo.orm.sqlparser.daoresolve.parameterresolver;

import java.util.Map;

public interface DaoParamResolver {

    public void handleParam(Map<String, Object> namedParams, Object[] objects);

}
