package com.silentgo.core.exception;

/**
 * Project : silentgo
 * com.silentgo.core.exception
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/29.
 */
public class AppParameterPaserException extends AppException {
    public AppParameterPaserException(String message, int code) {
        super(message, code);
    }

    public AppParameterPaserException(String message) {
        super(message);
    }

    public AppParameterPaserException(String message, Object... objects) {
        super(String.format(message, objects));
    }
}
