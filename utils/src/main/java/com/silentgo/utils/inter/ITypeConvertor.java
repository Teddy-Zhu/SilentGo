package com.silentgo.utils.inter;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public interface ITypeConvertor<S, T> {
    T convert(S source, Object... objects);
}
