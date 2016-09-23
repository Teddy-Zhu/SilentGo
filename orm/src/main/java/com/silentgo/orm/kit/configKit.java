package com.silentgo.orm.kit;

import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.common.Const;
import com.silentgo.utils.PropKit;

/**
 * Project : silentgo
 * com.silentgo.orm.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/21.
 */
public class configKit {

    public static DBConfig getConfig(String name) {
        PropKit propKit = new PropKit(name);

        DBConfig dbConfig = new DBConfig();

        dbConfig.setDriver(propKit.getValue(Const.driver));

        dbConfig.setUrl(propKit.getValue(Const.url));
        dbConfig.setUrl(propKit.getValue(Const.url));
        dbConfig.setUserName(propKit.getValue(Const.user));
        dbConfig.setPassword(propKit.getValue(Const.password));
        dbConfig.setMinActive(5);
        dbConfig.setMaxActive(10);

        dbConfig.setMaxIdle(100000);
        return dbConfig;
    }
}
