package ru.inurgalimov.http.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.inurgalimov.http.adapter.AnswerAdapter;
import ru.inurgalimov.http.exception.NotFoundPathException;
import ru.inurgalimov.http.exception.UnsupportedMethodException;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.response.HttpResponse;
import ru.inurgalimov.http.utils.HttpStatus;
import ru.inurgalimov.http.utils.HttpVersion;
import ru.inurgalimov.http.utils.Method;
import ru.inurgalimov.http.utils.UrlParsingUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MainHandler implements Handler {

    private final Map<Method, Map<String, Function<HttpRequest, Object>>> routeMap;
    private final Map<Class<?>, AnswerAdapter<?>> adapters;

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        Map<String, Function<HttpRequest, Object>> functionMap = Optional.ofNullable(routeMap.get(request.getMethod()))
                .orElseThrow(() ->
                        new UnsupportedMethodException(String.format("Method %s unsupported", request.getMethod())));
        String path = UrlParsingUtils.getPath(request.getUri());
        Object result = Optional.ofNullable(functionMap.get(path))
                .map(function -> function.apply(request))
                .orElseThrow(() -> new NotFoundPathException(String.format("Not found handler for path: %s", path)));
        AnswerAdapter<?> adapter = Objects.isNull(result) ? adapters.get(null) :
                adapters.getOrDefault(result.getClass(), adapters.get(Object.class));
        byte[] body = adapter.objectToBytes(result);
        response.setStatus(HttpStatus.OK);
        response.setVersion(HttpVersion.HTTP_VERSION_11);
        Map<String, String> header = new HashMap<>(adapter.getHeader());
        header.put("Content-Length", String.valueOf(body.length));
        response.setHeaders(header);
        response.setBody(body);
    }

}
