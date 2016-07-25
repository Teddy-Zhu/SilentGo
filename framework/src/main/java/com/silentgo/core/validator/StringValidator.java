package com.silentgo.core.validator;

import com.silentgo.core.route.annotation.RequestParam;
import com.silentgo.core.validator.annotation.RequestString;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : silentgo
 * com.silentgo.core.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class StringValidator implements IValidator<RequestString> {
    @Override
    public boolean validate(Response response, Request request, RequestParam requestParam, RequestString validator, Object arg, Object[] args) {



        return false;
    }
}
