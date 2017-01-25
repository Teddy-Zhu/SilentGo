package com.silentgo.core.aop.validator.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.exception.ValidateException;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.aop.validator
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/18.
 */
@Intercept
public class ValidatorInterceptor implements Interceptor {

    private static final Log LOGGER = LogFactory.get();

    @Override
    public int priority() {
        return 15;
    }

    @Override
    public Object resolve(AOPPoint point) throws Throwable {
        Long start = System.currentTimeMillis();
        MethodParam[] params = point.getAdviser().getParams();
        ValidatorFactory validatorFactory = SilentGo.me().getFactory(ValidatorFactory.class);
        Map<String, Map<Annotation, IValidator>> validateMap = validatorFactory.getParamValidatorMap(point.getAdviser().getName());
        if (validateMap != null) {
            for (int i = 0, len = params.length; i < len; i++) {
                MethodParam param = params[i];
                Map<Annotation, IValidator> map = validateMap.get(param.getName());
                if (map == null) {
                    LOGGER.debug("param:{}, ivalidator not found", param.getName());
                    continue;
                }
                for (Map.Entry<Annotation, IValidator> entry : map.entrySet()) {
                    Annotation annotation = entry.getKey();
                    IValidator validator = entry.getValue();
                    if (!validator.validate(point.getResponse(), point.getRequest(), annotation, point.getObjects()[i], i, point.getObjects())) {
                        throw new ValidateException(String.format("Parameter [%s] validate error", param.getName()));
                    }
                }
            }
        } else {
            LOGGER.debug("method :{} , validatorMap not found");
        }

        Object ret = point.proceed();
        LOGGER.debug("end validator Intercept : {}", System.currentTimeMillis() - start);
        return ret;

    }
}
