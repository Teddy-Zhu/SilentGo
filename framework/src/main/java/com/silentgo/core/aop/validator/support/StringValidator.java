package com.silentgo.core.aop.validator.support;

import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.annotation.RequestString;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class StringValidator implements IValidator<RequestString> {

    @Override
    public boolean validate(Response response, Request request, RequestString param, Object arg) {
        return false;
    }
}
