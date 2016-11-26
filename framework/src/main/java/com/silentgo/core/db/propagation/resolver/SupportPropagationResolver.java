package com.silentgo.core.db.propagation.resolver;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.core.db.intercept.Transaction;
import com.silentgo.core.db.propagation.PropagationKit;
import com.silentgo.core.db.propagation.PropagationResolver;
import com.silentgo.orm.base.DBConnect;

import java.sql.Connection;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.db.propagation.resolver
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/25.
 */
public class SupportPropagationResolver implements PropagationResolver {
    @Override
    public Object resolve(SilentGo me, AnnotationInterceptChain chain, Transaction annotation, DBConnect connect, String name, boolean hasConnect) throws Throwable {
        if (hasConnect) {
            Connection innerConnect = connect.getConnect();
            boolean isAuto = innerConnect.getAutoCommit();

            if (!isAuto) {
                if (innerConnect.getTransactionIsolation() < annotation.transcationLevel())
                    innerConnect.setTransactionIsolation(annotation.transcationLevel());
            }
            return chain.intercept();
        } else {
            return PropagationKit.resolve(me, chain, connect, annotation);
        }

    }
}