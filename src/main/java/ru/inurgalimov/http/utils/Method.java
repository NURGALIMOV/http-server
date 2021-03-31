package ru.inurgalimov.http.utils;

import ru.inurgalimov.http.exception.BadHeaderException;

public enum Method {
    GET,
    POST;

    public static Method valueOfByString(String method) {
        for (Method m : Method.values()) {
            if (method.equals(m.name())) {
                return m;
            }
        }
        throw new BadHeaderException("invalid header: " + method);
    }

}
