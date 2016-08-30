package com.silentgo.core.exception;

/**
 * Project : silentgo
 * com.silentgo.core.exception
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/8/29.
 */
public class AppRouteDispatchException extends AppException {
    public AppRouteDispatchException(String message, int code) {
        super(message, code);
    }

    public AppRouteDispatchException(String message) {
        super(message);
    }
}
