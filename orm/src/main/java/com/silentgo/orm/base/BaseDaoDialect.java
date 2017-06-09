package com.silentgo.orm.base;

import java.util.Collection;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge.mysql
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public interface BaseDaoDialect {
    //QUERY
    public SQLTool queryByPrimaryKey(BaseTableInfo table, Object id);

    public SQLTool queryByPrimaryKeys(BaseTableInfo table, Collection<Object> ids);

    public <T extends TableModel> SQLTool queryByModelSelective(BaseTableInfo table, T t);

    public <T extends TableModel> SQLTool queryByModelMap(BaseTableInfo table, Map<String, Object> t);

    //ADD
    public <T extends TableModel> SQLTool insertByRow(BaseTableInfo table, T t);

    public <T extends TableModel> SQLTool insertByRows(BaseTableInfo table, Collection<T> t);

    //UPDATE
    public <T extends TableModel> SQLTool updateByPrimaryKey(BaseTableInfo table, T t);

    public <T extends TableModel> SQLTool updateByPrimaryKeyOptional(BaseTableInfo table, T t, Collection<String> columns);

    public <T extends TableModel> SQLTool updateByPrimaryKeySelective(BaseTableInfo table, T t);

    //DELETE
    public SQLTool deleteByPrimaryKey(BaseTableInfo table, Object id);

    public SQLTool deleteByPrimaryKeys(BaseTableInfo table, Collection<Object> ids);

    public SQLTool queryAll(BaseTableInfo tableInfo);

    public SQLTool deleteAll(BaseTableInfo tableInfo);

    //Count
    public SQLTool countByModelMap(BaseTableInfo table, Map<String, Object> t);

}
