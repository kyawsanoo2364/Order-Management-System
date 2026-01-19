package com.vodica.order_system.exceptions;

public class CustomWebServiceException extends RuntimeException {
    public CustomWebServiceException() {
    }

    public CustomWebServiceException(String message) {
        super(message);
    }

    public CustomWebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomWebServiceException(Throwable cause) {
        super(cause);
    }

    public CustomWebServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
