package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.Set;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/28.
 */
public class SetDaoResolver implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return parsedMethod.contains(DaoKeyWord.Set.innername);
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled,
                                                     BaseDaoDialect daoDialect, Map<String, Object> nameObjects, Method method) {
        int set = parsedMethod.indexOf(DaoKeyWord.Set.innername);
        String filed = DaoResolveKit.getField(parsedMethod, set + 1);
        if (DaoResolveKit.isField(filed, tableInfo))
            set(set, DaoKeyWord.And.innername, parsedMethod, tableInfo, sqlTool);

        Optional<Annotation> opSet = annotations.stream().filter(annotation -> annotation.annotationType().equals(Set.class)).findFirst();
        if (opSet.isPresent()) {
            Set setAn = (Set) opSet.get();
            for (String s : setAn.value()) {
                sqlTool.set(SQLKit.buildParam(s, objectIndex, objects, sqlTool, nameObjects));
            }
        }
        return sqlTool;
    }


    public void set(int index, String string, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool) {
        if (DaoKeyWord.And.equals(string)) {
            String field = DaoResolveKit.getField(parsedMethod, tableInfo, index + 1);
            sqlTool.setEqual(field);
            Integer next = index + 2;
            set(next, parsedMethod.get(next), parsedMethod, tableInfo, sqlTool);
        }
    }
}
