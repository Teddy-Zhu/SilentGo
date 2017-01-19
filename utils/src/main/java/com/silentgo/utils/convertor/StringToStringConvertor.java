package com.silentgo.utils.convertor;

import com.silentgo.utils.inter.ITypeConvertor;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/9.
 */
public class StringToStringConvertor implements ITypeConvertor<String, String> {


    @Override
    public String convert(String source, Object... objects) {
        return source;
    }
}
