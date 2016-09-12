package com.silentgo.core.typeconvert.support;


import com.silentgo.core.typeconvert.ITypeConvertor;
import com.silentgo.core.typeconvert.annotation.Convertor;

import java.util.Date;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/27.
 */
@Convertor
public class StringToDateConvertor implements ITypeConvertor<String, Date> {
    @Override
    public Date convert(String source) {
        return null;
    }
}
