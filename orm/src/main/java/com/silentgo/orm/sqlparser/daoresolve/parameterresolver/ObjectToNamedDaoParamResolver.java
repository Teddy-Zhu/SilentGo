package com.silentgo.orm.sqlparser.daoresolve.parameterresolver;

import com.silentgo.orm.sqlparser.daoresolve.ResolvedParam;

import java.util.Map;

public class ObjectToNamedDaoParamResolver implements DaoParamResolver {

    private ResolvedParam resolvedParam;


    public ObjectToNamedDaoParamResolver(ResolvedParam resolvedParam) {
        this.resolvedParam = resolvedParam;
    }

    @Override
    public void handleParam(Map<String, Object> namedParams, Object[] objects) {
        if (namedParams != null)
            namedParams.put(resolvedParam.getName(), objects[resolvedParam.getIndex()]);
    }
}
