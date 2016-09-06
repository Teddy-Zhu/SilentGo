package com.silentgo.core.route.support.annotationResolver.support;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.exception.AppException;
import com.silentgo.core.route.annotation.RouteMatch;
import com.silentgo.core.route.support.annotationResolver.ParamAnnotationResolver;
import com.silentgo.core.route.support.annotationResolver.annotation.ParamAnResolver;
import com.silentgo.kit.StringKit;
import com.silentgo.servlet.http.ContentType;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.RequestMethod;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.annotationResolver.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
@ParamAnResolver
public class RouteMatchAnResolver implements ParamAnnotationResolver<RouteMatch> {
    @Override
    public boolean validate(MethodAdviser adviser, Response response, Request request, RouteMatch annotaion) throws AppException {
        //validate method

        validateRequestMethod(annotaion.method(), request);


        validateRequestHeader(annotaion.headers(), request);


        validateProduces(annotaion.produces(), request);


        validateConsumes(annotaion.consumes(), request);
        return true;
    }

    private void validateConsumes(String[] consumes, Request request) throws AppException {
        for (String consume : consumes) {
            if (!request.getContentType().contains(consume)) {
                throw new AppException(405);
            }
        }
    }

    private void validateProduces(String[] produces, Request request) throws AppException {
        String accept = request.getHeader("Accept");
        if (StringKit.isNullOrEmpty(accept) && produces.length > 0)
            throw new AppException(405);
        for (String produce : produces) {
            if (!accept.contains(produce))
                throw new AppException(405);
        }
    }

    private void validateRequestHeader(String[] headers, Request request) throws AppException {
        for (String header : headers) {
            String[] heads = header.split("=");
            if (heads.length == 1) {
                if (!StringKit.isNullOrEmpty(request.getHeader(heads[0]))) {
                    return;
                }
            } else if (heads.length == 2) {
                if (request.getHeader(heads[0]).equals(heads[1])) {
                    return;
                }
            }
        }
        throw new AppException(405);
    }

    private void validateRequestMethod(RequestMethod[] methods, Request request) throws AppException {
        RequestMethod method = RequestMethod.prase(request.getMethod());
        for (RequestMethod requestMethod : methods) {
            if (method.equals(requestMethod)) return;
        }
        throw new AppException(405);
    }
}
