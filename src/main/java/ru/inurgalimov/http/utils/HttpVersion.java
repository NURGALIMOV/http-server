package ru.inurgalimov.http.utils;

import ru.inurgalimov.http.exception.BadHeaderException;

public enum HttpVersion {

    HTTP_VERSION_11("HTTP/1.1");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public static HttpVersion valueOfByString(String version) {
        for (HttpVersion v : HttpVersion.values()) {
            if (v.getVersion().equals(version)) {
                return v;
            }
        }
        throw new BadHeaderException("invalid version: " + version);
    }

}
