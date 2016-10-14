package com.silentgo.core.aop.validator.support;

import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.annotation.RequestBool;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.core.config.Const;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.aop.validator.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/13.
 */
@Validator
public class BoolValidator implements IValidator<RequestBool> {
    @Override
    public boolean validate(Response response, Request request, RequestBool param, Object arg, int index, Object[] objects) {

        if (arg == null) {
            if (!Const.DEFAULT_NONE.equals(param.defaultValue())) {
                objects[index] = Boolean.parseBoolean(param.defaultValue());
                arg = objects[index];
            }
        }
        if (param.required() && arg == null) {
            return false;
        }
        return true;
    }
}
