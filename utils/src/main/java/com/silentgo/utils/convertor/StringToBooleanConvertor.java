package com.silentgo.utils.convertor;

import com.silentgo.utils.common.Const;
import com.silentgo.utils.inter.ITypeConvertor;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
public class StringToBooleanConvertor implements ITypeConvertor<String, Boolean> {
    @Override
    public Boolean convert(String source) {
        if (source == null) return null;
        if (Const.EmptyString.equals(source.trim())) return Boolean.FALSE;

        return Boolean.parseBoolean(source) || source.equalsIgnoreCase("1");
    }
}
