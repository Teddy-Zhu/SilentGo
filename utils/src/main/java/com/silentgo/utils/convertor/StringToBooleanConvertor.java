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
    public Boolean convert(String source, Object... objects) {
        if (source == null) return null;

        if ((source.equalsIgnoreCase("true")) ||
                (source.equalsIgnoreCase("on")) ||
                (source.equalsIgnoreCase("yes"))) {
            return true;
        } else if ((source.equalsIgnoreCase("false")) ||
                (source.equalsIgnoreCase("off")) ||
                (source.equalsIgnoreCase("no"))) {
            return false;
        } else {
            throw new NumberFormatException("Parameter " + source + " can not be number");
        }
    }
}
