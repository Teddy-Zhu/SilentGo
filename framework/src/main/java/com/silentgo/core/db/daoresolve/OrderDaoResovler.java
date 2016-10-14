package com.silentgo.core.db.daoresolve;

import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.db.funcanalyse.DaoKeyWord;
import com.silentgo.core.exception.AppSQLException;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
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
    public <T extends TableModel> SQLTool processSQL(String methodName, Class<?> returnType, Object[] objects, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool, List<Annotation> annotations, boolean[] isHandled) throws AppSQLException {
        int index = parsedMethod.indexOf(DaoKeyWord.Order.innername);
        String two = parsedMethod.get(index + 1);
        if (DaoKeyWord.By.equals(two)) {
            setOrder(index + 1, DaoKeyWord.And.innername, parsedMethod, tableInfo, sqlTool);
        } else {
            throw new AppSQLException("error syntax : after Order : " + two);
        }
        return null;
    }

    public void setOrder(int index, String string, List<String> parsedMethod, BaseTableInfo tableInfo, SQLTool sqlTool) throws AppSQLException {
        if (DaoKeyWord.And.equals(string)) {
            String f = DaoResolveKit.getField(parsedMethod, tableInfo, index + 1);
            String sort = parsedMethod.get(index + 2);
            if (DaoKeyWord.Desc.equals(sort)) {
                sqlTool.orderByDesc(f);
                index += 1;
            } else if (DaoKeyWord.Asc.equals(sort)) {
                sqlTool.orderByAsc(f);
                index += 1;
            }
            Integer nextIndex = index + 2;
            setOrder(nextIndex, parsedMethod.get(nextIndex), parsedMethod, tableInfo, sqlTool);
        } else {
            return;
        }
    }
}
