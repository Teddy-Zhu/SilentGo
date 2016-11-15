package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.GroupBy;
import com.silentgo.orm.sqlparser.annotation.OrderBy;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/30.
 */
public class GroupDaoResovler implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return parsedMethod.contains(DaoKeyWord.Group.innername);
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType,
                                                     Object[] objects, Integer[] objectIndex, List<String> parsedMethod,
                                                     BaseTableInfo tableInfo, SQLTool sqlTool,
                                                     List<Annotation> annotations, boolean[] isHandled,
                                                     BaseDaoDialect baseDaoDialect, Map<String, Object> nameObjects) {
        int index = parsedMethod.indexOf(DaoKeyWord.Group.innername);
        String two = DaoResolveKit.getField(parsedMethod, index + 1);
        if (DaoKeyWord.By.equals(two)) {
            String field = DaoResolveKit.getField(parsedMethod, index + 2);
            if (DaoResolveKit.isField(field, tableInfo))
                setGroup(index + 1, DaoKeyWord.And.innername, parsedMethod, tableInfo, sqlTool);
        }
        Optional<Annotation> opGroupBy = annotations.stream().filter(annotation -> annotation.annotationType().equals(GroupBy.class)).findFirst();
        if (opGroupBy.isPresent()) {
            GroupBy groupBy = (GroupBy) opGroupBy.get();
            for (String s : groupBy.value()) {
                sqlTool.groupBy(SQLKit.buildParam(s, objectIndex, objects, sqlTool, nameObjects));
            }
        }
        return sqlTool;
    }

    public void setGroup(int index, String string, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool) {
        if (DaoKeyWord.And.equals(string)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 1);
            sqlTool.groupBy(f);
            Integer nextIndex = index + 2;
            setGroup(nextIndex, DaoResolveKit.getField(parsedMethod, nextIndex), parsedMethod, tableInfo, sqlTool);
        }
    }
}
