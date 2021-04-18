package ru.inurgalimov.http.server;

import ru.inurgalimov.http.exception.NotUniquePathException;
import ru.inurgalimov.http.handler.RequestHandler;
import ru.inurgalimov.http.handler.RequestHandlerImpl;
import ru.inurgalimov.http.handler.ResponseHandler;
import ru.inurgalimov.http.handler.ResponseHandlerImpl;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.response.HttpResponse;
import ru.inurgalimov.http.utils.Method;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static ru.inurgalimov.http.utils.Method.GET;
import static ru.inurgalimov.http.utils.Method.POST;

public class ServerImpl extends AbstractServer {

    private final Map<Method, Map<String, BiConsumer<HttpRequest, HttpResponse>>> handlersStorage = new EnumMap<>(Method.class);
    private final RequestHandler requestHandler = new RequestHandlerImpl();
    private final ResponseHandler responseHandler = new ResponseHandlerImpl();

    public ServerImpl() {
        Arrays.stream(Method.values())
                .forEach(method -> handlersStorage.put(method, new HashMap<>()));
    }

    public void registerGetHandler(String path, BiConsumer<HttpRequest, HttpResponse> handler) {
        registerHandler(path, handler, handlersStorage.get(GET));
    }

    public void registerPostHandler(String path, BiConsumer<HttpRequest, HttpResponse> handler) {
        registerHandler(path, handler, handlersStorage.get(POST));
    }

    private void registerHandler(String path, BiConsumer<HttpRequest, HttpResponse> handler,
                                 Map<String, BiConsumer<HttpRequest, HttpResponse>> storage) {
        if (storage.containsKey(path)) {
            throw new NotUniquePathException("This path has already was registered before");
        }
        storage.put(path, handler);
    }

    @Override
    protected void handle(InputStream in, OutputStream out) throws IOException {
        HttpRequest request = requestHandler.handleRequest(in);
        HttpResponse response = HttpResponse.builder()
                .build();
        handlersStorage.get(request.getMethod())
                .getOrDefault(request.getUri(), this::defaultHandling)
                .accept(request, response);
        responseHandler.handleResponse(response, out);
    }

}