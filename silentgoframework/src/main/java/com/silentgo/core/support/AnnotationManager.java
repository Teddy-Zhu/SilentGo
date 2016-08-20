package com.silentgo.core.support;

import com.silentgo.config.SilentGoConfig;
import com.silentgo.kit.ClassKit;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Project : silentgo
 * com.silentgo.core.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/25.
 */
public class AnnotationManager {

    private Map<Class<? extends Annotation>, Set<Class>> anMap;

    public AnnotationManager(SilentGoConfig config) {
        this.anMap = ClassKit.searchAnnotation(config.getScanPackages(), config.getScanJars());
    }

    public Set<Class> getClasses(Class<? extends Annotation> an) {
        return anMap.containsKey(an) ? anMap.get(an) : new HashSet<>();
    }

}
