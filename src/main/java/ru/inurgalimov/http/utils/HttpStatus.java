package ru.inurgalimov.http.utils;

import lombok.Getter;

@Getter
public enum HttpStatus {

    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported");

    private final int code;
    private final String statusText;


    HttpStatus(int code, String statusText) {
        this.code = code;
        this.statusText = statusText;
    }

}

