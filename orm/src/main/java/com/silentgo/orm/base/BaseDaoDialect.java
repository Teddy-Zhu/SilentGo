package com.silentgo.orm.base;

import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.TableModel;

import java.util.List;

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

    public SQLTool queryByPrimaryKeys(BaseTableInfo table, List<Object> ids);

    public <T extends TableModel> SQLTool queryByModelSelective(BaseTableInfo table, T t);

    //ADD
    public <T extends TableModel> SQLTool insertByRow(BaseTableInfo table, T t);

    public <T extends TableModel> SQLTool insertByRows(BaseTableInfo table, List<T> t);

    //UPDATE
    public <T extends TableModel> SQLTool updateByPrimaryKey(BaseTableInfo table, T t);

    public <T extends TableModel> SQLTool updateByPrimaryKeyOptional(BaseTableInfo table, T t, List<String> columns);

    public <T extends TableModel> SQLTool updateByPrimaryKeySelective(BaseTableInfo table, T t);

    //DELETE
    public SQLTool deleteByPrimaryKey(BaseTableInfo table, Object id);

    public SQLTool deleteByPrimaryKeys(BaseTableInfo table, List<Object> ids);

    public SQLTool queryAll(BaseTableInfo tableInfo);

    public SQLTool deleteAll(BaseTableInfo tableInfo);
}
