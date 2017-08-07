package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/9/28.
 */
public class CommonDaoResolver implements DaoResolver {
    private static final ArrayList<String> methodNames = new ArrayList() {{
        add("queryByPrimaryKey");
        add("queryByPrimaryKeys");
        add("queryByModelSelective");
        add("queryByModelMap");
        add("insertByRow");
        add("insertByRows");
        add("updateByPrimaryKey");
        add("updateByPrimaryKeyOptional");
        add("updateByPrimaryKeySelective");
        add("deleteByPrimaryKey");
        add("deleteByPrimaryKeys");
        add("queryAll");
        add("deleteAll");
        add("queryCustom");
        add("countCustom");
        add("countByModelMap");
    }};

    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return methodNames.contains(daoResolveEntity.getMethodName());
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        daoResolveEntity.resolved();
        BaseDaoDialect daoDialect = daoResolveEntity.getDaoDialect();
        BaseTableInfo tableInfo = daoResolveEntity.getTableInfo();
        Object[] objects = daoResolveEntity.getObjects();
        SQLTool sqlTool = null;
        switch (daoResolveEntity.getMethodName()) {
            case "queryByPrimaryKey": {
                sqlTool = daoDialect.queryByPrimaryKey(tableInfo, objects[0]);
                break;
            }
            case "queryByPrimaryKeys": {
                sqlTool = daoDialect.queryByPrimaryKeys(tableInfo, (Collection<Object>) objects[0]);
                break;
            }
            case "queryByModelSelective": {
                sqlTool = daoDialect.queryByModelSelective(tableInfo, (T) objects[0]);
                break;
            }
            case "queryByModelMap": {
                sqlTool = daoDialect.queryByModelMap(tableInfo, (Map<String, Object>) objects[0]);
                break;
            }
            case "insertByRow": {
                sqlTool = daoDialect.insertByRow(tableInfo, (T) objects[0]);
                break;
            }
            case "insertByRows": {
                sqlTool = daoDialect.insertByRows(tableInfo, (Collection<T>) objects[0]);
                break;
            }
            case "updateByPrimaryKey": {

                sqlTool = daoDialect.updateByPrimaryKey(tableInfo, (T) objects[0]);
                break;
            }
            case "updateByPrimaryKeyOptional": {
                sqlTool = daoDialect.updateByPrimaryKeyOptional(tableInfo, (T) objects[0], (Collection<String>) objects[1]);
                break;
            }
            case "updateByPrimaryKeySelective": {
                sqlTool = daoDialect.updateByPrimaryKeySelective(tableInfo, (T) objects[0]);
                break;
            }
            case "deleteByPrimaryKey": {
                sqlTool = daoDialect.deleteByPrimaryKey(tableInfo, objects[0]);
                break;
            }
            case "deleteByPrimaryKeys": {
                sqlTool = daoDialect.deleteByPrimaryKeys(tableInfo, (Collection<Object>) objects[0]);
                break;
            }
            case "queryAll": {
                sqlTool = daoDialect.queryAll(tableInfo);
                break;
            }
            case "deleteAll": {
                sqlTool = daoDialect.deleteAll(tableInfo);
                break;
            }
            case "queryCustom": {
                sqlTool = (SQLTool) objects[0];
                break;
            }
            case "countCustom": {
                sqlTool = (SQLTool) objects[0];
                break;
            }
            case "countByModelMap": {
                sqlTool = daoDialect.countByModelMap(tableInfo, (Map<String, Object>) objects[0]);
                break;
            }
        }
        if (sqlTool == null)
            daoResolveEntity.setHandled(false);
        else {
            sqlTool.setParams(daoResolveEntity.getNameObjects());
            sqlTool.setObjects(daoResolveEntity.getObjects());
            daoResolveEntity.setSqlTool(sqlTool);
        }
    }

}
