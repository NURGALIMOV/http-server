package ru.inurgalimov.http.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.inurgalimov.http.adapter.AnswerAdapter;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.response.HttpResponse;
import ru.inurgalimov.http.utils.HttpStatus;
import ru.inurgalimov.http.utils.HttpVersion;
import ru.inurgalimov.http.utils.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MainHandler implements Handler {

    private final Map<Method, Map<String, Function<HttpRequest, Object>>> routMap;
    private final Map<Class<?>, AnswerAdapter<?>> adapters;

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        Object result = routMap.get(request.getMethod()).get(request.getUri()).apply(request);
        AnswerAdapter<?> adapter = Objects.isNull(result) ? adapters.get(null) :
                adapters.getOrDefault(result.getClass(), adapters.get(Object.class));
        byte[] body = adapter.objectToBytes(result);
        response.setStatus(HttpStatus.OK);
        response.setVersion(HttpVersion.HTTP_VERSION_11);
        final Map<String, String> header = new HashMap<>(adapter.getHeader());
        header.put("Content-Length", String.valueOf(body.length));
        response.setHeaders(header);
        response.setBody(body);
    }

}
