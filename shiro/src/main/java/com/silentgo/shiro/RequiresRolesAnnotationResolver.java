package com.silentgo.shiro;

import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;

/**
 * Created by teddyzhu on 15/12/14.
 */
@CustomInterceptor
public class RequiresRolesAnnotationResolver implements IAnnotation<RequiresRoles> {


    @Override
    public Object intercept(AnnotationInterceptChain chain, Response response, Request request, RequiresRoles requiresRoles) throws Throwable {
        String[] roles = requiresRoles.value();
        Subject subject = SecurityUtils.getSubject();
        if (roles.length == 1) {
            subject.checkRole(roles[0]);
            return chain.intercept();
        }
        if (Logical.AND.equals(requiresRoles.logical())) {
            subject.checkRoles(Arrays.asList(roles));
            return chain.intercept();
        }
        if (Logical.OR.equals(requiresRoles.logical())) {
            // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
            boolean hasAtLeastOneRole = false;
            for (String role : roles) if (subject.hasRole(role)) hasAtLeastOneRole = true;
            // Cause the exception if none of the role match, note that the exception message will be a bit misleading
            if (!hasAtLeastOneRole) subject.checkRole(roles[0]);
        }
        return chain.intercept();
    }
}
