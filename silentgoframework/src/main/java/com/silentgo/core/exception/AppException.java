package com.silentgo.core.exception;

/**
 * Project : silentgo
 * com.silentgo.core.exception
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class AppException extends Exception {

    public int code = 500;

    public AppException(String message, int code) {
        super(message);
    }

    public AppException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
