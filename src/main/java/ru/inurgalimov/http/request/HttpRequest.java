package ru.inurgalimov.http.request;

import lombok.Builder;
import lombok.Getter;
import ru.inurgalimov.http.utils.HttpVersion;
import ru.inurgalimov.http.utils.Method;

import java.util.Map;

@Getter
@Builder
public class HttpRequest {

    private final Method method;
    private final String uri;
    private final HttpVersion version;
    private final Map<String, String> headers;
    private final byte[] body;

}
