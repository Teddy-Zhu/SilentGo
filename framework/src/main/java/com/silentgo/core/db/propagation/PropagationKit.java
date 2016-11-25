package com.silentgo.core.db.propagation;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.core.db.intercept.Transaction;
import com.silentgo.orm.base.DBConnect;

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

    public static Object resolve(SilentGo me, AnnotationInterceptChain chain, DBConnect connect, Transaction annotation) throws Throwable {
        Object ret = null;
        Connection innerConnect = connect.getConnect();

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
            me.releaseConnect();
        }
        return ret;
    }
}
