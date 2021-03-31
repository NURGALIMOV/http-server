package ru.inurgalimov.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.inurgalimov.http.exception.BadHeaderException;
import ru.inurgalimov.http.exception.MalFormedRequestException;
import ru.inurgalimov.http.exception.NotUniquePathException;
import ru.inurgalimov.http.exception.UnsupportedMethodException;
import ru.inurgalimov.http.handler.RequestHandler;
import ru.inurgalimov.http.handler.RequestHandlerImpl;
import ru.inurgalimov.http.handler.ResponseHandler;
import ru.inurgalimov.http.handler.ResponseHandlerImpl;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.response.HttpResponse;
import ru.inurgalimov.http.utils.HttpStatus;
import ru.inurgalimov.http.utils.HttpVersion;
import ru.inurgalimov.http.utils.Method;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.BiConsumer;

import static ru.inurgalimov.http.utils.Method.GET;
import static ru.inurgalimov.http.utils.Method.POST;

public class Server {

    private final Logger logger = LoggerFactory.getLogger(Server.class);
    private final Map<Method, Map<String, BiConsumer<HttpRequest, HttpResponse>>> handlersStorage = new EnumMap<>(Method.class);
    private final RequestHandler requestHandler = new RequestHandlerImpl();
    private final ResponseHandler responseHandler = new ResponseHandlerImpl();

    public Server() {
        Arrays.stream(Method.values()).forEach(method -> handlersStorage.put(method, new HashMap<>()));
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

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    final var socket = serverSocket.accept();
                    handleConnection(socket);
                } catch (RuntimeException e) {
                    logger.info("Handling request error", e);
                } catch (IOException e) {
                    logger.error("Socket server error", e);
                }
            }
        } catch (IOException e) {
            logger.error("Error creating socket server", e);
        }
    }

    private void handleConnection(Socket socket) throws IOException {
        try (socket;
             final var in = new BufferedInputStream(socket.getInputStream());
             final var out = new BufferedOutputStream(socket.getOutputStream())) {

            HttpRequest request = requestHandler.handleRequest(in);
            HttpResponse response = HttpResponse.builder().build();
            handlersStorage.get(request.getMethod())
                    .getOrDefault(request.getUri(), this::defaultHandling)
                    .accept(request, response);
            responseHandler.handleResponse(response, out);
        }
    }

    private void defaultHandling(HttpRequest request, HttpResponse response) {
        response.setVersion(HttpVersion.HTTP_VERSION_11);
        response.setBody(new byte[0]);
        response.setHeaders(Map.of("Content-Length", "0"));
        response.setStatus(HttpStatus.NOT_FOUND);
    }

}