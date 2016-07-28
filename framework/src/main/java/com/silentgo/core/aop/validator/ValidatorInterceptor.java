package com.silentgo.core.aop.validator;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.validator.exception.ValidateException;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
public class ValidatorInterceptor implements Interceptor {

    @Override
    public boolean build(SilentGo me) {
        return false;
    }

    @Override
    public Object resolve(AOPPoint point, boolean[] isResolved) throws Throwable {
        List<MethodParam> params = point.getAdviser().getParams();

        for (MethodParam param : params) {
            Map<Annotation, IValidator> map = param.getValidatorMap();
            for (Map.Entry<Annotation, IValidator> entry : map.entrySet()) {
                Annotation annotation = entry.getKey();
                IValidator validator = entry.getValue();
                if (!validator.validate(point.getResponse(), point.getRequest(), annotation, param.getValue(point.getRequest()))) {
                    throw new ValidateException(String.format("Parameter [%s] validate error", param.getName()));
                }
            }
        }

        return point.resolve();

    }
}
