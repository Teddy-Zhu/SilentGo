package com.silentgo.core.typeconvert.support;


import com.silentgo.core.typeconvert.ITypeConvertor;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/29.
 */
public class CommonConvertor implements ITypeConvertor<Object, Object> {
    @Override
    public Object convert(Object source) {
        return source;
    }
}
