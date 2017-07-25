package com.silentgo.orm.base;

import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.plugin.db.bridge
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/22.
 */
public interface BaseDao<T extends TableModel, K> {

    public int countCustom(SQLTool sqlTool);

    public int countByModelMap(Map<String, Object> map);

    public List<T> queryCustom(SQLTool sqlTool);

    //QUERY
    public T queryByPrimaryKey(K id);

    public List<T> queryByPrimaryKeys(List<K> ids);

    public List<T> queryByModelSelective(T t);

    public List<T> queryByModelMap(Map<String, Object> map);

    //ADD
    public int insertByRow(T t);

    public int insertByRows(List<T> t);

    //UPDATE
    public int updateByPrimaryKey(T t);

    public int updateByPrimaryKeyOptional(T t, List<String> columns);

    public int updateByPrimaryKeySelective(T t);

    //DELETE
    public int deleteByPrimaryKey(K t);

    public int deleteByPrimaryKeys(List<K> t);


    //Query All
    public List<T> queryAll();

    //Delete All
    public List<T> deleteAll();
}
