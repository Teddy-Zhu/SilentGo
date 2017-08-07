package com.silentgo.orm.sqlparser;

import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.Pager;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.annotation.Param;
import com.silentgo.orm.sqlparser.daoresolve.ResolvedParam;
import com.silentgo.orm.sqlparser.daoresolve.parameterresolver.ObjectToNamedDaoParamResolver;
import com.silentgo.orm.sqltool.SqlToken;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.orm.sqltool.sqltoken.ParamSqlToken;
import com.silentgo.orm.sqltool.sqltoken.StringSqlToken;
import com.silentgo.utils.Assert;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.reflect.SGMethod;
import com.silentgo.utils.reflect.SGParameter;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/10/28.
 */
public class SQLKit {
    public static final String PagerName = "_sql_pager";
    public static final String objectPrefix = "_index_";
    public static final String nameRegex = "<#(.*?)/>";
    public static final Pattern namedPattern = Pattern.compile(nameRegex);

    public static final Pattern namedPatternAll = Pattern.compile("<#(.*?)/>");
    public static final String parameter = "?";

    private static final Map<Method, Map<Integer, String>> cachedNameParams = new HashMap<>();

    public static Object getObject(DaoResolveEntity daoResolveEntity) {
        Object o = daoResolveEntity.getObjectIndex() >= daoResolveEntity.getObjects().length ? null : daoResolveEntity.getObjects()[daoResolveEntity.getObjectIndex()];
        daoResolveEntity.setObjectIndex(daoResolveEntity.getObjectIndex() + 1);
        return o;
    }

    public static String buildParamString(int size) {
        StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < size - 1; i++) {
            sb.append("?,");
        }
        if (size > 0) {
            sb.append("? ");
        }
        return sb.toString();
    }

    public static Object[] parseNamedObject(SGMethod sgMethod, Object[] originObjects, Map<String, Object> namedObject) {
//        Map<Integer, String> nameMap = cachedNameParams.get(sgMethod.getMethod());
//        List<Object> common
//        if (nameMap == null) {
//            nameMap = new HashMap<>();
//            String[] parametersNames = sgMethod.getParameterNames();
//            for (int i = 0; i < parametersNames.length; i++) {
//                String name = parametersNames[i];
//                SGParameter parameter = sgMethod.getParameterMap().get(name);
//                Param param = parameter.getAnnotation(Param.class);
//                String realName = param == null ? name : param.value();
//                namedObject.put(realName, originObjects[i]);
//                nameMap.put(i, realName);
//            }
//            cachedNameParams.put(sgMethod.getMethod(), nameMap);
//        } else {
//            for (int i = 0; i < originObjects.length; i++) {
//                if (nameMap.containsKey(i)) {
//                    namedObject.put(nameMap.get(i), originObjects[i]);
//                }
//            }
//        }

        Map<Integer, String> nameMap = cachedNameParams.get(sgMethod.getMethod());
        List<Object> commonObject = new ArrayList<>();
        if (nameMap == null) {
            nameMap = new HashMap<>();
            String[] parametersNames = sgMethod.getParameterNames();

            for (int i = 0, len = parametersNames.length; i < len; i++) {
                String name = parametersNames[i];
                SGParameter parameter = sgMethod.getParameterMap().get(name);

                Param param = parameter.getAnnotation(Param.class);
                if (param != null) {
                    String realName = param.value();

                    nameMap.put(i, realName);
                    namedObject.put(realName, originObjects[i]);
                } else {
                    if (originObjects[i] instanceof Pager) {
                        nameMap.put(i, PagerName);
                        namedObject.put(PagerName, originObjects[i]);
                    } else {
                        commonObject.add(originObjects[i]);
                    }
                }
            }
            cachedNameParams.put(sgMethod.getMethod(), nameMap);
        } else {
            for (int i = 0; i < originObjects.length; i++) {
                if (nameMap.containsKey(i)) {
                    namedObject.put(nameMap.get(i), originObjects[i]);
                } else {
                    commonObject.add(originObjects[i]);
                }
            }
        }
        return commonObject.toArray();
    }


    public static void buildSQL(SqlTokenGroup sqlTokenGroup, String sql) {

        Matcher matcher = SQLKit.namedPattern.matcher(sql);
        int start = 0;
        String pre = null;
        while (matcher.find()) {
            pre = sql.substring(start, matcher.start());
            if (StringKit.isNotEmpty(pre)) {
                sqlTokenGroup.appendToken(new StringSqlToken(pre));
            }
            start = matcher.end();
            String name = matcher.group(1);
            Assert.isNotBlank(name, "the parameter name must be not blank");
            name = name.trim();
            sqlTokenGroup.appendToken(new ParamSqlToken(name));
        }

        pre = sql.substring(start);
        if (StringKit.isNotEmpty(pre)) {
            sqlTokenGroup.appendToken(new StringSqlToken(pre));
        }
    }

    public static ResolvedParam resolveObject(DaoResolveEntity daoResolveEntity) {
        int index = daoResolveEntity.getObjectParsedIndex();
        String name = SQLKit.objectPrefix + index;
        ResolvedParam resolvedParam = new ResolvedParam(name, index);
        daoResolveEntity.increaseParsedObjectIndex();
        daoResolveEntity.getSqlTool().addParamResolver(new ObjectToNamedDaoParamResolver(resolvedParam));
        return resolvedParam;
    }
}
