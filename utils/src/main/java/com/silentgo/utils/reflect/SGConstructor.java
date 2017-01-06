package com.silentgo.utils.reflect;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.reflect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class SGConstructor extends ParameterAnnotationMap {

    private boolean isDefault;

    private Constructor constructor;

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
