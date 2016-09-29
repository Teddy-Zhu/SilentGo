package com.silentgo.core.db.intercept;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : parent
 * Package : com.silentgo.core.aop.annotationintercept.innerimpl
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/27.
 */
@CustomInterceptor
public class TransactionInterceptor implements IAnnotation<Transaction> {

    @Override
    public Object intercept(AnnotationInterceptChain chain, Response response, Request request, Transaction annotation) throws Throwable {
        DBConnect connect = SilentGo.getInstance().getConnect();

        if (!connect.getConnect().getAutoCommit()) {
            connect.getConnect().setAutoCommit(false);
        }
        Object ret = null;
        try {
            ret = chain.intercept();
            connect.getConnect().commit();
        } catch (Exception e) {
            for (Class<? extends Exception> aClass : annotation.rollback()) {
                if (aClass.isAssignableFrom(e.getClass())) {
                    connect.getConnect().rollback();
                    break;
                }
            }
            throw e;
        } finally {
            connect.getConnect().setAutoCommit(true);
        }
        return ret;
    }
}
