package com.silentgo.core.aop.validator.support;

import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.annotation.RequestString;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.core.config.Const;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import com.silentgo.utils.StringKit;

import java.util.regex.Pattern;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Validator
public class StringValidator implements IValidator<RequestString> {

    @Override
    public boolean validate(Response response, Request request, RequestString param, Object arg, int index, Object[] objects) {
        String value = arg.toString();

        if (StringKit.isBlank(value)) {
            if (!Const.DEFAULT_NONE.equals(param.defaultValue())) {
                objects[index] = param.defaultValue();
                value = param.defaultValue();
            }
        }
        if (StringKit.isBlank(value) && param.required()) {
            return false;
        }

        if (value.length() < param.range()[0] || value.length() > param.range()[1]) {
            return false;
        }

        if (!Const.DEFAULT_NONE.equals(param.regex())) {
            if (!Pattern.compile(param.regex()).matcher(value).matches()) {
                return false;
            }
        }
        return true;
    }
}
