package ru.inurgalimov.http;

public class Main {
    public static void main(String[] args) {
        final var server = new Server();
        server.listen(9999);
    }
}
