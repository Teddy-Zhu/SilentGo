package com.silentgo.core.exception;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.exception
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/12/7.
 */
public class AppParseExcetion extends AppException {
    public AppParseExcetion(String message, int code) {
        super(message, code);
    }

    public AppParseExcetion(int code) {
        super(code);
    }

    public AppParseExcetion(String message) {
        super(message);
    }
}
