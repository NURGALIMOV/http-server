package ru.inurgalimov.http.exception;

public class UnsupportedMethodException extends RuntimeException {

    public UnsupportedMethodException() {
        super();
    }

    public UnsupportedMethodException(String message) {
        super(message);
    }

    public UnsupportedMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedMethodException(Throwable cause) {
        super(cause);
    }

    protected UnsupportedMethodException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
