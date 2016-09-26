package com.silentgo.core.exception;

/**
 * Project : silentgo
 * com.silentgo.core.exception
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/26.
 */
public class AppReleaseException extends AppException {
    public AppReleaseException(String message, int code) {
        super(message, code);
    }

    public AppReleaseException(int code) {
        super(code);
    }

    public AppReleaseException(String message) {
        super(message);
    }
}
