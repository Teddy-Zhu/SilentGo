package com.silentgo.core.aop.validator.support;

import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptor;
import com.silentgo.core.aop.validator.IValidator;
import com.silentgo.core.aop.validator.exception.ValidateException;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLog(AnnotationInterceptor.class);


    @Override
    public int priority() {
        return 15;
    }

    @Override
    public Object resolve(AOPPoint point) throws Throwable {
        LOGGER.debug("start validator Intercept");
        MethodParam[] params = point.getAdviser().getParams();
        ValidatorFactory validatorFactory = SilentGo.getInstance().getFactory(ValidatorFactory.class);
        Map<String, Map<Annotation, IValidator>> validateMap = validatorFactory.getParamValidatorMap(point.getAdviser().getName());
        for (MethodParam param : params) {
            Map<Annotation, IValidator> map = validateMap.get(param.getName());
            for (Map.Entry<Annotation, IValidator> entry : map.entrySet()) {
                Annotation annotation = entry.getKey();
                IValidator validator = entry.getValue();
                if (!validator.validate(point.getResponse(), point.getRequest(), annotation, param.getValue(point.getRequest()))) {
                    throw new ValidateException(String.format("Parameter [%s] validate error", param.getName()));
                }
            }
        }

        Object ret = point.doChain();
        LOGGER.debug("end validator Intercept");
        return ret;

    }
}
