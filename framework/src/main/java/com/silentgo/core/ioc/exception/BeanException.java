package com.silentgo.core.ioc.exception;

import com.silentgo.core.exception.AppException;

public class BeanException extends AppException {
    public BeanException(String message, int code) {
        super(message, code);
    }

    public BeanException(int code) {
        super(code);
    }

    public BeanException(String message) {
        super(message);
    }
}
