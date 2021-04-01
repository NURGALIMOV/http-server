package ru.inurgalimov.http.server;

import ru.inurgalimov.http.handler.RequestHandler;
import ru.inurgalimov.http.handler.ResponseHandler;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.response.HttpResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;

public class ServerWithMainRequestHandler extends AbstractServer {


    private BiConsumer<HttpRequest, HttpResponse> mainRequestHandler;
    private final RequestHandler requestHandler;
    private final ResponseHandler responseHandler;

    public ServerWithMainRequestHandler(RequestHandler requestHandler, ResponseHandler responseHandler) {
        this.requestHandler = requestHandler;
        this.responseHandler = responseHandler;
    }


    public void registerHandler(BiConsumer<HttpRequest, HttpResponse> handler) {
        mainRequestHandler = handler;
    }

    @Override
    protected void handle(BufferedInputStream in, BufferedOutputStream out) throws IOException {
        HttpRequest request = requestHandler.handleRequest(in);
        HttpResponse response = HttpResponse.builder().build();
        mainRequestHandler.accept(request, response);
        responseHandler.handleResponse(response, out);
    }

}
