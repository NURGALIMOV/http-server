package ru.inurgalimov.http.exception;

public class UnsupportedRequestLineException extends RuntimeException {

    public UnsupportedRequestLineException() {
        super();
    }

    public UnsupportedRequestLineException(String message) {
        super(message);
    }

    public UnsupportedRequestLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedRequestLineException(Throwable cause) {
        super(cause);
    }

    protected UnsupportedRequestLineException(String message, Throwable cause, boolean enableSuppression,
                                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
