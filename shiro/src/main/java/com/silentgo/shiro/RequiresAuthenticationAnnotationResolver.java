package com.silentgo.shiro;

import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

/**
 * Created by teddyzhu on 15/12/14.
 */
@CustomInterceptor
public class RequiresAuthenticationAnnotationResolver implements IAnnotation<RequiresAuthentication> {


    @Override
    public Object intercept(AnnotationInterceptChain chain, Response response, Request request, RequiresAuthentication annotation) throws Throwable {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            throw new UnauthenticatedException("The current Subject is not authenticated.  Access denied.");
        }
        return chain.intercept();
    }
}
