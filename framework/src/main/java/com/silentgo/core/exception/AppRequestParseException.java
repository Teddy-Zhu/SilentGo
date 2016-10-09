package com.silentgo.core.exception;

/**
 * Project : parent
 * Package : com.silentgo.core.exception
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/9.
 */
public class AppRequestParseException extends AppException {
    public AppRequestParseException(String message, int code) {
        super(message, code);
    }

    public AppRequestParseException(int code) {
        super(code);
    }

    public AppRequestParseException(String message) {
        super(message);
    }
}
