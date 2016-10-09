package com.silentgo.core.route.support.annotationresolver.support;

import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.exception.AppException;
import com.silentgo.core.route.annotation.RouteMatch;
import com.silentgo.core.route.support.annotationresolver.RouteAnnotationResolver;
import com.silentgo.core.route.support.annotationresolver.annotation.RouteAnResolver;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.RequestMethod;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.StringKit;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.annotationresolver.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
@RouteAnResolver
public class RouteMatchAnResolver implements RouteAnnotationResolver<RouteMatch> {
    @Override
    public boolean validate(MethodAdviser adviser, Response response, Request request, RouteMatch annotaion) throws AppException {
        //validate method

        validateRequestMethod(annotaion.method(), request);


        validateRequestHeader(annotaion.headers(), request);


        validateProduces(annotaion.produces(), request, response);


        validateConsumes(annotaion.consumes(), request);
        return true;
    }

    private void validateConsumes(String[] consumes, Request request) throws AppException {
        String contentType = request.getContentType();
        for (String consume : consumes) {
            if (!contentType.contains(consume)) {
                throw new AppException(405);
            }
        }
    }

    private void validateProduces(String[] produces, Request request, Response response) throws AppException {
        String accept = request.getHeader("Accept");
        if (StringKit.isBlank(accept) || produces.length > 0)
            throw new AppException(405);
        if (accept.contains("*/*")) return;
        for (String produce : produces) {
            if (!accept.contains(produce))
                throw new AppException(405);
            response.setContentType(response.getContentType().endsWith(";") ?
                    (response.getContentType() + produce) : (response.getContentType() + ";" + produce));
        }
    }

    private void validateRequestHeader(String[] headers, Request request) throws AppException {
        for (String header : headers) {
            String head0 = StringKit.getLeft(header, "=");
            String head1 = StringKit.getRight(header, "=");
            if (StringKit.isNotBlank(head0) && StringKit.isNotBlank(head1)) return;
            if (!StringKit.isBlank(request.getHeader(head0))) {
                continue;
            }
            if (!request.getHeader(head0).equals(head1)) {
                throw new AppException(405);
            }
        }
    }

    private void validateRequestMethod(RequestMethod[] methods, Request request) throws AppException {
        RequestMethod method = RequestMethod.prase(request.getMethod());
        for (RequestMethod requestMethod : methods) {
            if (method.equals(requestMethod)) return;
        }
        throw new AppException(405);
    }
}
