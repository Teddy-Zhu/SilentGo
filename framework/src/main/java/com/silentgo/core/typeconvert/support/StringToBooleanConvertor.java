package com.silentgo.core.typeconvert.support;

import com.silentgo.core.config.Const;
import com.silentgo.core.typeconvert.ITypeConvertor;
import com.silentgo.core.typeconvert.annotation.Convertor;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
@Convertor
public class StringToBooleanConvertor implements ITypeConvertor<String, Boolean> {
    @Override
    public Boolean convert(String source) {
        if (source == null) return null;
        if (Const.EmptyString.equals(source.trim())) return Boolean.FALSE;

        return Boolean.parseBoolean(source) || source.equalsIgnoreCase("1");
    }
}
