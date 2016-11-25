package com.silentgo.core.db.intercept;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.core.config.Const;
import com.silentgo.core.db.propagation.resolver.NotSupportPropagationResolver;
import com.silentgo.core.db.propagation.resolver.RequiredPropagationResolver;
import com.silentgo.core.db.propagation.resolver.RequiresNewPropagationResolver;
import com.silentgo.core.db.propagation.resolver.SupportPropagationResolver;
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
        SilentGo me = SilentGo.me();
        String name = Const.DEFAULT_NONE.equals(annotation.value()) ? me.getConfig().getDbType() : annotation.value();

        boolean hasConnect = me.hasConnecct();
        DBConnect connnect = me.getConnect(name);
        switch (annotation.propagation()) {
            case PROPAGATION_REQUIRED:
                return new RequiredPropagationResolver().resolve(me, chain, annotation, connnect, name, hasConnect);
            case PROPAGATION_NOT_SUPPORTED:
                return new NotSupportPropagationResolver().resolve(me, chain, annotation, connnect, name, hasConnect);
            case PROPAGATION_REQUIRES_NEW:
                return new RequiresNewPropagationResolver().resolve(me, chain, annotation, connnect, name, hasConnect);
            case PROPAGATION_SUPPORTS:
                return new SupportPropagationResolver().resolve(me, chain, annotation, connnect, name, hasConnect);
        }
        return null;
    }

}
