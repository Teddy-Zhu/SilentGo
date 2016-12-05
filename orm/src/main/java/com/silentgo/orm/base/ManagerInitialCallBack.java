package com.silentgo.orm.base;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.base
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/12/5.
 */
public interface ManagerInitialCallBack {
    void before(DBManager manager);

    void after(DBManager manager);
}
