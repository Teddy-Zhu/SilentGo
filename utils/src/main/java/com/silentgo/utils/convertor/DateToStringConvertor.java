package com.silentgo.utils.convertor;

import com.silentgo.utils.inter.ITypeConvertor;

import java.util.Date;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.convertor
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/4/19.
 */
public class DateToStringConvertor implements ITypeConvertor<Date, String> {
    @Override
    public String convert(Date source, Object... objects) {
        return source == null ? "null" : String.valueOf(source.getTime());
    }
}
