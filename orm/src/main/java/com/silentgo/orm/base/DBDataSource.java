package com.silentgo.orm.base;

import javax.sql.DataSource;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.base
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/25.
 */
public interface DBDataSource extends DataSource {

    public String getName();

    public void setName(String name);

    public DBConfig getConfig();

    public void setConfig(DBConfig config);

}
