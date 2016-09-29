package com.silentgo.core.exception;

/**
 * Project : parent
 * Package : com.silentgo.core.exception
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/28.
 */
public class AppSQLException extends AppException {
    public AppSQLException(String message, int code) {
        super(message, code);
    }

    public AppSQLException(int code) {
        super(code);
    }

    public AppSQLException(String message) {
        super(message);
    }
}
