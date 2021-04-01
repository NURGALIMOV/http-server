package ru.inurgalimov.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer implements Server {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void listen(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
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

    protected void handleConnection(Socket socket) throws IOException {
        try (socket;
             final var in = new BufferedInputStream(socket.getInputStream());
             final var out = new BufferedOutputStream(socket.getOutputStream())) {
            handle(in, out);
        }
    }

    protected abstract void handle(InputStream in, OutputStream out) throws IOException;

}
