package com.silentgo.core.exception;

/**
 * Project : silentgo
 * com.silentgo.core.exception
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/29.
 */
public class AppParameterResolverException extends AppException {
    public AppParameterResolverException(String message, int code) {
        super(message, code);
    }

    public AppParameterResolverException(String message) {
        super(message);
    }
}
