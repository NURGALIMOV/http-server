package ru.inurgalimov.http.utils;

import java.util.Arrays;
import java.util.Optional;

public enum HttpMethod {
    GET,
    POST;

    public static Optional<HttpMethod> valueOfByString(String method) {
        return Arrays.stream(HttpMethod.values())
                .filter(m -> method.equals(m.name()))
                .findFirst();
    }

}
