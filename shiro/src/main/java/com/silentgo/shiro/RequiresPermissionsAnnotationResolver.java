package com.silentgo.shiro;

import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.annotation.CustomInterceptor;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptChain;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

/**
 * Created by teddyzhu on 15/12/14.
 */
@CustomInterceptor
public class RequiresPermissionsAnnotationResolver implements IAnnotation<RequiresPermissions> {


    @Override
    public Object intercept(AnnotationInterceptChain chain, Response response, Request request, RequiresPermissions permissions) throws Throwable {
        String[] perms = permissions.value();
        Subject subject = SecurityUtils.getSubject();

        if (perms.length == 1) {
            subject.checkPermission(perms[0]);
            return chain.intercept();
        }
        if (Logical.AND.equals(permissions.logical())) {
            subject.checkPermissions(perms);
            return chain.intercept();
        }
        if (Logical.OR.equals(permissions.logical())) {
            // Avoid processing exceptions unnecessarily - "delay" throwing the
            // exception by calling hasRole first
            boolean hasAtLeastOnePermission = false;
            for (String permission : perms)
                if (subject.isPermitted(permission))
                    hasAtLeastOnePermission = true;
            // Cause the exception if none of the role match, note that the
            // exception message will be a bit misleading
            if (!hasAtLeastOnePermission)
                subject.checkPermission(perms[0]);
        }
        return chain.intercept();
    }
}
