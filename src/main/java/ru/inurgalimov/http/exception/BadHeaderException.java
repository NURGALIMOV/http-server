package ru.inurgalimov.http.exception;

public class BadHeaderException extends RuntimeException {

    public BadHeaderException() {
        super();
    }

    public BadHeaderException(String message) {
        super(message);
    }

    public BadHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadHeaderException(Throwable cause) {
        super(cause);
    }

    protected BadHeaderException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
