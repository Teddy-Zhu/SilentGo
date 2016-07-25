package com.silentgo.core.validator;

import com.silentgo.core.route.annotation.RequestParam;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.lang.annotation.Annotation;

/**
 * Project : silentgo
 * com.silentgo.core.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public interface IValidator<T extends Annotation> {

    boolean validate(Response response, Request request, RequestParam requestParam, T param, Object arg, Object[] args);
}
