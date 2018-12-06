package com.yl.core.http.exception;


public class ApiException extends RuntimeException {
    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        cause.getMessage();
    }
}
