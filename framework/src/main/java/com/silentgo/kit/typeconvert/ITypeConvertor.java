package com.silentgo.kit.typeconvert;

/**
 * Project : silentgo
 * com.silentgo.kit.typeconvert
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by teddyzhu on 16/7/26.
 */
public interface ITypeConvertor<S, T> {
    T convert(S source);
}
