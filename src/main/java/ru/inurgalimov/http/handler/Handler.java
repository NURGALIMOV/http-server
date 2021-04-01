package ru.inurgalimov.http.handler;

import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Handler {

    void handle(HttpRequest request, HttpResponse response);

}
