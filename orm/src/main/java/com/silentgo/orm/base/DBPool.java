package com.silentgo.orm.base;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public interface DBPool {
    public DBConnect getDBConnect();

    public boolean releaseDBConnect(DBConnect connect);

    public boolean destory();

    public DBConnect getUnSafeDBConnect();

    public boolean releaseUnSafeDBConnect(DBConnect connect);

    public DBConnect getThreadConnect();

    public boolean setThreadConnect(DBConnect connect);


}
