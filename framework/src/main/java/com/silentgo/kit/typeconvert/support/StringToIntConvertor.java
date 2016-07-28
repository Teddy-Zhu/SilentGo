package com.silentgo.kit.typeconvert.support;

import com.silentgo.kit.typeconvert.ITypeConvertor;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public class StringToIntConvertor implements ITypeConvertor<String, Integer> {
    @Override
    public Integer convert(String source) {
        Integer target = null;
        try {
            target = Integer.valueOf(source);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return target;
    }
}
