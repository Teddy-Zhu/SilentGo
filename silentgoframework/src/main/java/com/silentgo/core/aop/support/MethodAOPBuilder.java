package com.silentgo.core.aop.support;

import com.silentgo.build.SilentGoBuilder;
import com.silentgo.build.annotation.Builder;
import com.silentgo.config.Const;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;

import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
@Builder
public class MethodAOPBuilder extends SilentGoBuilder {

    @Override
    public Integer priority() {
        return 20;
    }

    @Override
    public boolean build(SilentGo me) {

        MethodAOPFactory methodAOPFactory = new MethodAOPFactory();

        me.getConfig().addFactory(methodAOPFactory);

        InterceptFactory interceptFactory = (InterceptFactory) me.getConfig().getFactory(Const.InterceptFactory);

        methodAOPFactory.getMethodAdviserMap().forEach((k, v) -> {

            List interceptors = new ArrayList<Interceptor>() {{
                //add global
                addAll(me.getConfig().getInterceptors());
                //add class
                addAll(interceptFactory.getClassInterceptors().get(v.getClassName()));
                //add method
                addAll(interceptFactory.getMethodInterceptors().get(v.getName()));

            }};
            //for filter others


            //save method interceptors
            sortInterceptrs(interceptors);

            methodAOPFactory.addBuildedInterceptor(v.getName(), interceptors);

        });
        return true;
    }


    public void sortInterceptrs(List<Interceptor> interceptors) {
        interceptors.sort(((o1, o2) -> {
            int x = o1.priority();
            int y = o2.priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }));

    }

}
