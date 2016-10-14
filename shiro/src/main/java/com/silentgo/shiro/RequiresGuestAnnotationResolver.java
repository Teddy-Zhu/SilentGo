package com.silentgo.shiro;

import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresGuest;

/**
 * Created by teddyzhu on 15/12/14.
 */
@CustomInterceptor
public class RequiresGuestAnnotationResolver implements IAnnotation<RequiresGuest> {


    @Override
    public Object intercept(AnnotationInterceptChain chain, Response response, Request request, RequiresGuest annotation) throws Throwable {
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            throw new UnauthenticatedException("Attempting to perform a guest-only operation.  The current Subject is " +
                    "not a guest (they have been authenticated or remembered from a previous login).  Access " +
                    "denied.");
        }
        return chain.intercept();
    }
}
