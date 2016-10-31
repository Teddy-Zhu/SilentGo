package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseDaoDialect;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.OrderBy;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;

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
public class OrderDaoResovler implements DaoResolver {
    @Override
    public boolean handle(String methodName, List<String> parsedMethod, List<Annotation> annotations) {
        return parsedMethod.contains(DaoKeyWord.Order.innername);
    }

    @Override
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, Integer[] objectIndex, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled, BaseDaoDialect baseDaoDialect, Map<String, Object> nameObjects) {
        int index = parsedMethod.indexOf(DaoKeyWord.Order.innername);
        String two = DaoResolveKit.getField(parsedMethod, index + 1);
        if (DaoKeyWord.By.equals(two)) {
            if (DaoResolveKit.isField(parsedMethod.get(index + 2), tableInfo))
                setOrder(index + 1, DaoKeyWord.And.innername, parsedMethod, tableInfo, sqlTool);
        } else {
            throw new RuntimeException("error syntax : after Order : " + two);
        }
        Optional<Annotation> opOrderBy = annotations.stream().filter(annotation -> annotation.annotationType().equals(OrderBy.class)).findFirst();
        if (opOrderBy.isPresent()) {
            OrderBy orderBy = (OrderBy) opOrderBy.get();
            for (String s : orderBy.value()) {
                sqlTool.orderBy(SQLKit.buildParam(s, objectIndex, objects, sqlTool, nameObjects));
            }
        }
        return sqlTool;
    }

    public void setOrder(int index, String string, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool) {
        if (DaoKeyWord.And.equals(string)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 1);
            String sort = DaoResolveKit.getField(parsedMethod, index + 2);
            if (DaoKeyWord.Desc.equals(sort)) {
                sqlTool.orderByDesc(f);
                index += 1;
            } else if (DaoKeyWord.Asc.equals(sort)) {
                sqlTool.orderByAsc(f);
                index += 1;
            }
            Integer nextIndex = index + 2;
            setOrder(nextIndex, DaoResolveKit.getField(parsedMethod, nextIndex), parsedMethod, tableInfo, sqlTool);
        }
    }
}
