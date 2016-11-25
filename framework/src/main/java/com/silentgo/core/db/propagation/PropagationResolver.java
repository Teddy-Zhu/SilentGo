package com.silentgo.core.db.propagation;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.core.db.intercept.Transaction;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.sql.SQLException;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.db.propagation
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/25.
 */
public interface PropagationResolver {

    public Object resolve(SilentGo me, AnnotationInterceptChain chain, Transaction annotation,
                          DBConnect connect, String name, boolean hasConnect) throws Throwable;

}
