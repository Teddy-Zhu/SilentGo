package com.silentgo.utils.convertor;


import com.silentgo.utils.inter.ITypeConvertor;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public class StringToIntegerConvertor implements ITypeConvertor<String, Integer> {

    @Override
    public Integer convert(String source, Object... objects) {
        return Integer.valueOf(source);
    }
}
