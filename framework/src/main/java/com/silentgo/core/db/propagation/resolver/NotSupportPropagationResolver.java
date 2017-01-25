package com.silentgo.core.db.propagation.resolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.core.db.intercept.Transaction;
import com.silentgo.core.db.propagation.PropagationKit;
import com.silentgo.core.db.propagation.PropagationResolver;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBType;
import com.silentgo.orm.connect.ConnectManager;

import java.sql.Connection;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.db.propagation.resolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/25.
 */
public class NotSupportPropagationResolver implements PropagationResolver {
    @Override
    public Object resolve(SilentGo me, AnnotationInterceptChain chain, Transaction annotation, DBConnect connect, String name, boolean hasConnect) throws Throwable {
        if (hasConnect) {
            Connection innerConnect = connect.getConnect();

            if (innerConnect.getAutoCommit()) {
                return chain.intercept();
            } else {
                DBType type = DBType.parse(me.getConfig().getDbType());
                DBConnect newConnect = ConnectManager.me().getNewConnect(type, name);

                ConnectManager.me().setThreadConnect(type, name, newConnect);
                Object ret = chain.intercept();
                ConnectManager.me().releaseNewConnect(type, name, newConnect);
                ConnectManager.me().setThreadConnect(type, name, connect);
                return ret;
            }
        } else {
            return chain.intercept();
        }
    }
}
