package com.silentgo.orm.base;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public interface DBManager {

    public void initial(DBConfig... configs);

    public DBPool getPool(String name);

    public DBConnect getConnect(String name);

    public boolean releaseConnect(String name, DBConnect connect);

    public boolean destory();

}
