package com.silentgo.orm.sqlparser;

import com.silentgo.orm.base.NameParam;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.annotation.Param;
import com.silentgo.utils.StringKit;

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
 *         <p>
 *         Created by teddyzhu on 2016/10/28.
 */
public class SQLKit {
    public static final String nameRegex = "<#(.*?)/>";
    public static final Pattern namedPattern = Pattern.compile(nameRegex);

    public static final Pattern namedPatternAll = Pattern.compile("(\\?)|<#(.*?)/>");
    public static final String parameter = "?";

    private static final Map<Method, Map<Integer, String>> cachedNameParams = new HashMap<>();

    public static SQLTool buildParams(SQLTool sqlTool, Object[] commomObjects, Map<String, Object> namedObject) {
        String sql = sqlTool.getSQL();
        Matcher matcher = namedPattern.matcher(sqlTool.getSQL());
        List<Object> newObject = new ArrayList<>();
        int commonIndex = 0, searchStart = 0;
        while (matcher.find()) {
            String name = matcher.group(1).trim();
            int end = matcher.start();
            int location = StringKit.indexOf(sql, parameter, searchStart, end);
            while (location != -1) {
                newObject.add(commomObjects[commonIndex++]);
                location = StringKit.indexOf(sql, parameter, location + 1, end);
            }
            searchStart = matcher.end();
            newObject.add(namedObject.get(name));
        }
        for (int i = commonIndex, size = commomObjects.length; i < size; i++) {
            newObject.add(commomObjects[i]);
        }


        return new SQLTool(sql.replaceAll(nameRegex, parameter), newObject);
    }


    public static Object getObject(Integer[] index, Object[] args) {
        return index[0] >= args.length ? null : args[index[0]++];
    }

    public static String buildParamString(int size) {
        StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < size - 1; i++) {
            sb.append("?,");
        }
        sb.append("? ");
        return sb.toString();
    }

    public static Object[] getNamedObject(Method method, Object[] originObjects, Map<String, Object> namedObject) {
        Map<Integer, String> nameMap = cachedNameParams.get(method);
        List<Object> commonObject = new ArrayList<>();
        if (nameMap == null) {
            nameMap = new HashMap<>();
            Parameter[] parameters = method.getParameters();
            for (int i = 0, len = parameters.length; i < len; i++) {
                Parameter parameter = parameters[i];
                Param param = parameter.getAnnotation(Param.class);
                if (param != null && StringKit.isNotBlank(param.value())) {
                    nameMap.put(i, param.value());
                    namedObject.put(param.value(), originObjects[i]);
                } else {
                    commonObject.add(originObjects[i]);
                }
            }
            cachedNameParams.put(method, nameMap);
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

    public static String buildParam(String s, Integer[] objectIndex, Object[] objects, SQLTool sqlTool, Map<String, Object> nameObjects) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = SQLKit.namedPatternAll.matcher(s);
        int start = 0;
        while (matcher.find()) {
            String pd = matcher.group(0);
            sb.append(s.substring(start, matcher.start()));
            start = matcher.end();
            if (pd.equals(SQLKit.parameter)) {
                Object arg = SQLKit.getObject(objectIndex, objects);
                if (arg instanceof Collection) {
                    sb.append(SQLKit.buildParamString(((Collection) arg).size()));
                    ((Collection) arg).forEach(sqlTool::appendParam);
                } else {
                    sb.append(pd);
                    sqlTool.appendParam(arg);
                }
            } else {
                String name = matcher.group(2);
                Object arg = nameObjects.get(name);
                if (arg instanceof Collection) {
                    sb.append(SQLKit.buildParamString(((Collection) arg).size()));
                    ((Collection) arg).forEach(sqlTool::appendParam);
                } else {
                    sb.append("?");
                    sqlTool.appendParam(arg);
                }
            }
        }
        sb.append(s.substring(start));
        return sb.toString();
    }
}
