package ru.inurgalimov.http.handler;

import ru.inurgalimov.http.request.HttpRequest;

import java.io.IOException;
import java.io.InputStream;

public interface RequestHandler {

    HttpRequest handleRequest(InputStream input) throws IOException;

}
