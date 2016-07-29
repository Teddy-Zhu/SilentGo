package com.silentgo.core.aop.validator;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.aop.annotationintercept.AnnotationInterceptor;
import com.silentgo.core.aop.support.InterceptChain;
import com.silentgo.core.aop.validator.annotation.Validator;
import com.silentgo.core.aop.validator.exception.ValidateException;
import com.silentgo.core.aop.validator.support.ValidatorFactory;
import com.silentgo.kit.ClassKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLog(AnnotationInterceptor.class);

    @Override
    public boolean build(SilentGo me) {
        me.getAnnotationManager().getClasses(Validator.class).forEach(aClass -> {
            if (IValidator.class.isAssignableFrom(aClass)) {
                Class<? extends Annotation> an = (Class<? extends Annotation>) ClassKit.getGenericClass(aClass, 0);
                try {
                    if (ValidatorFactory.addValidator(an, (IValidator) aClass.newInstance())) {
                        if (me.getConfig().isDevMode()) {
                            LOGGER.debug("Register Custom Validator [{}] successfully", aClass.getName());
                        }
                    } else {
                        LOGGER.error("Register Custom Validator [{}] failed", aClass.getName());
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
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

        return point.doChain();

    }
}
