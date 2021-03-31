package ru.inurgalimov.http.exception;

public class NotUniquePathException extends RuntimeException {

    public NotUniquePathException() {
        super();
    }

    public NotUniquePathException(String message) {
        super(message);
    }

    public NotUniquePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniquePathException(Throwable cause) {
        super(cause);
    }

    protected NotUniquePathException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
