package com.silentgo.orm.base;

import com.silentgo.orm.base.SQLTool;
import com.silentgo.orm.base.TableModel;

import java.util.List;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public interface BaseDao<T extends TableModel> {

    public <S extends T> int countCustom(SQLTool sqlTool);

    public <S extends T> List<T> queryCustom(SQLTool sqlTool);

    //QUERY
    public <S extends T> T queryByPrimaryKey(Object id);

    public <S extends T> List<S> queryByPrimaryKeys(List<Object> ids);

    public <S extends T> List<S> queryByModelSelective(S t);

    //ADD
    public <S extends T> int insertByRow(S t);

    public <S extends T> int insertByRows(List<S> t);

    //UPDATE
    public <S extends T> int updateByPrimaryKey(S t);

    public <S extends T> int updateByPrimaryKeyOptional(S t, List<String> columns);

    public int updateByPrimaryKeySelective(T t);

    //DELETE
    public int deleteByPrimaryKey(Object t);

    public int deleteByPrimaryKeys(List<Object> t);


    //Query All
    public <S extends T> List<S> queryAll();

    //Delete All
    public <S extends T> List<S> deleteAll();
}