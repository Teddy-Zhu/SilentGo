package com.silentgo.core.aop.support;

import com.silentgo.build.Builder;
import com.silentgo.core.SilentGo;
import com.silentgo.core.aop.Interceptor;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.annotationintercept.AnnotationInceptFactory;
import com.silentgo.core.aop.aspect.support.AspectFactory;
import com.silentgo.core.aop.validator.support.ValidatorFactory;
import com.silentgo.kit.CollectionKit;

import java.util.*;

/**
 * Project : silentgo
 * com.silentgo.core.aop.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
public class MethodAOPBuilder extends Builder {

    private MethodAOPBuilder() {
    }


    @Override
    public boolean build(SilentGo me) {

        MethodAOPFactory.getMethodAdviserMap().forEach((k, v) -> {

            List interceptors = new ArrayList<Interceptor>() {{
                //add global
                addAll(me.getConfig().getInterceptors());
                //add class
                addAll(InterceptFactory.getClassInterceptors().get(v.getClassName()));
                //add method
                addAll(InterceptFactory.getMethodInterceptors().get(v.getName()));

            }};
            //for filter others


            //save method interceptors
            sortInterceptrs(interceptors);

            MethodAOPFactory.addBuildedInterceptor(v.getName(), interceptors);

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
