package ru.inurgalimov.http.handler;

import ru.inurgalimov.http.response.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public interface ResponseHandler {

    void handleResponse(HttpResponse response, OutputStream output) throws IOException;

}