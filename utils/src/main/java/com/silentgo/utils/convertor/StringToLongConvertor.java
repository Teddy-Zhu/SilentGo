package com.silentgo.utils.convertor;


import com.silentgo.utils.inter.ITypeConvertor;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
public class StringToLongConvertor implements ITypeConvertor<String, Long> {

    @Override
    public Long convert(String source, Object... objects) {
        return Long.valueOf(source);
    }
}
