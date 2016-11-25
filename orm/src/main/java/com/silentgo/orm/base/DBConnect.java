package com.silentgo.orm.base;

import java.sql.Connection;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public interface DBConnect {

    public Connection getConnect();

    public boolean destroy();
}
