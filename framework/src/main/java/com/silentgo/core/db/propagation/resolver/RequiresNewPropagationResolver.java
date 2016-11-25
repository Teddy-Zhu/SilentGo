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
public class RequiresNewPropagationResolver implements PropagationResolver {
    @Override
    public Object resolve(SilentGo me, AnnotationInterceptChain chain, Transaction annotation, DBConnect connect, String name, boolean hasConnect) throws Throwable {
        if (hasConnect) {
            Connection innerConnect = connect.getConnect();
            boolean isAuto = innerConnect.getAutoCommit();

            if (isAuto) {
                return PropagationKit.resolve(me, chain, connect, annotation);
            } else {
                DBType type = DBType.parse(me.getConfig().getDbType());
                DBConnect newConnect = ConnectManager.me().getNewConnect(type, name);
                Connection newInnerConnect = newConnect.getConnect();
                ConnectManager.me().setTheadConnect(type, name, newConnect);

                newInnerConnect.setAutoCommit(false);
                newInnerConnect.setTransactionIsolation(annotation.transcationLevel());

                Object ret;
                try {
                    ret = chain.intercept();
                    newInnerConnect.commit();
                } catch (Throwable throwable) {
                    for (Class<? extends Exception> aClass : annotation.rollback()) {
                        if (aClass.isAssignableFrom(throwable.getClass())) {
                            innerConnect.rollback();
                            break;
                        }
                    }
                    throw throwable;
                } finally {
                    newInnerConnect.setAutoCommit(true);
                    ConnectManager.me().releaseNewConnect(type, name, newConnect);
                    ConnectManager.me().setTheadConnect(type, name, connect);
                }
                return ret;
            }
        } else {
            return PropagationKit.resolve(me, chain, connect, annotation);
        }
    }
}
