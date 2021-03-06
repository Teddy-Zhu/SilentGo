package com.silentgo.core.aop.aspect.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.AOPPoint;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.annotation.Intercept;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

/**
 * Project : silentgo
 * com.silentgo.core.aop.aspect.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/29.
 */
public class AspectInterceptor implements Interceptor {

    private static final Log LOGGER = LogFactory.get();
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public Object resolve(AOPPoint point) throws Throwable {
        Long start = System.currentTimeMillis();
        AspectFactory aspectFactory = SilentGo.me().getFactory(AspectFactory.class);
        Object ret = new AspectChain(point, aspectFactory.getAspectMethod(point.getAdviser().getName())).invoke();
        LOGGER.debug("end Aspect intercept : {}", System.currentTimeMillis() - start);
        return ret;
    }
}
