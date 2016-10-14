package com.silentgo.shiro;

import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresUser;

/**
 * Created by teddyzhu on 15/12/14.
 */
@CustomInterceptor
public class RequiresUserAnnotationResolver implements IAnnotation<RequiresUser> {


    @Override
    public Object intercept(AnnotationInterceptChain chain, Response response, Request request, RequiresUser annotation) throws Throwable {
        if (SecurityUtils.getSubject().getPrincipal() == null) {
            throw new UnauthenticatedException("Attempting to perform a user-only operation.  The current Subject is " +
                    "not a user (they haven't been authenticated or remembered from a previous login).  " +
                    "Access denied.");
        }
        return chain.intercept();
    }
}
