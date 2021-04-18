package ru.inurgalimov.http.exception;

public class NotFoundPathException extends RuntimeException {

    public NotFoundPathException() {
        super();
    }

    public NotFoundPathException(String message) {
        super(message);
    }

    public NotFoundPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundPathException(Throwable cause) {
        super(cause);
    }

    protected NotFoundPathException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
