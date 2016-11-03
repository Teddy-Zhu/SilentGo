package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.Where;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/28.
 */
public class WhereDaoResolver implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return parsedMethod.contains(DaoKeyWord.Where.innername);
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled,
                                                     BaseDaoDialect daoDialect, Map<String, Object> nameObjects) {
        int index = parsedMethod.indexOf(DaoKeyWord.Where.innername);
        if (DaoResolveKit.isField(parsedMethod.get(index + 1), tableInfo))
            setWhere(index, DaoKeyWord.And.innername, parsedMethod, tableInfo, sqlTool, objectIndex, objects);
        Optional<Annotation> opWhere = annotations.stream().filter(annotation -> annotation.annotationType().equals(Where.class)).findFirst();
        if (opWhere.isPresent()) {
            Where queryBy = (Where) opWhere.get();
            for (String s : queryBy.value()) {
                sqlTool.where(SQLKit.buildParam(s, objectIndex, objects, sqlTool, nameObjects));
            }
        }
        return sqlTool;
    }


    private void setWhere(int index, String string, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, Integer[] objectIndex, Object[] objects) {
        if (DaoKeyWord.And.equals(string)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 1);
            Object arg = SQLKit.getObject(objectIndex, objects);
            if (arg instanceof Collection) {
                sqlTool.whereIn(f, ((Collection) arg).size());
                ((Collection) arg).forEach(sqlTool::appendParam);
            } else {
                sqlTool.whereEquals(f).appendParam(arg);
            }
            Integer nextIndex = index + 2;
            setWhere(nextIndex, DaoResolveKit.getField(parsedMethod, nextIndex), parsedMethod, tableInfo, sqlTool, objectIndex, objects);
        }
    }


    public static void main(String[] args) {
        String a = "<#asd/>asdasad?xxxk<#asxx/>xxxx?xasd";

        Matcher matcher = SQLKit.namedPatternAll.matcher(a);

        while (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println("=======");
        }
    }
}
