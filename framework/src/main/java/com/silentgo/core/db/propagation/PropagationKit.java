package com.silentgo.core.db.propagation;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.core.db.intercept.Transaction;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBType;
import com.silentgo.orm.connect.ConnectManager;
import com.silentgo.utils.StringKit;

import java.sql.Connection;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.db.propagation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/25.
 */
public class PropagationKit {

    public static Object resolve(SilentGo me, AnnotationInterceptChain chain, DBConnect connect, Transaction annotation, String name) throws Throwable {
        Object ret;
        Connection innerConnect = connect.getConnect();
        DBType type = DBType.parse(me.getConfig().getDbType());

        innerConnect.setAutoCommit(false);
        innerConnect.setTransactionIsolation(annotation.transcationLevel());

        try {
            ret = chain.intercept();
            innerConnect.commit();
        } catch (Throwable throwable) {
            for (Class<? extends Exception> aClass : annotation.rollback()) {
                if (aClass.isAssignableFrom(throwable.getClass())) {
                    innerConnect.rollback();
                    break;
                }
            }
            throw throwable;
        } finally {
            innerConnect.setAutoCommit(true);
            ConnectManager.me().releaseConnect(type, name, connect);
        }
        return ret;
    }
}
