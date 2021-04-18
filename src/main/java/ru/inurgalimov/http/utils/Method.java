package ru.inurgalimov.http.utils;

import java.util.Arrays;
import java.util.Optional;

public enum Method {
    GET,
    POST;

    public static Optional<Method> valueOfByString(String method) {
        return Arrays.stream(Method.values())
                .filter(m -> method.equals(m.name()))
                .findFirst();
    }

}
