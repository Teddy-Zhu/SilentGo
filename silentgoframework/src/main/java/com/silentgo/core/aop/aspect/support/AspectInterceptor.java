package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.core.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;

/**
 * Project : silentgo
 * com.silentgo.core.aop.aspect.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/29.
 */
@Intercept
public class AspectInterceptor implements Interceptor {

    public static final Logger LOGGER = LoggerFactory.getLog(AspectInterceptor.class);

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public Object resolve(AOPPoint point) throws Throwable {
        LOGGER.debug("start Aspect intercept");
        AspectFactory aspectFactory = SilentGo.getInstance().getFactory(AspectFactory.class);
        Object ret = new AspectChain(point, aspectFactory.getAspectMethod(point.getAdviser().getName())).invoke();
        LOGGER.debug("end Aspect intercept");
        return ret;
    }
}
