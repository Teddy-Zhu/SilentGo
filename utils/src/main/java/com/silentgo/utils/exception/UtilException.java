package com.silentgo.utils.exception;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.exception
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/19.
 */
public class UtilException extends RuntimeException {

    public UtilException() {
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable cause) {
        super(message, cause);
    }
}
