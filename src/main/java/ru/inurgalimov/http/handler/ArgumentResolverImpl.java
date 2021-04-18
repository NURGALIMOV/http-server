package ru.inurgalimov.http.handler;

import lombok.RequiredArgsConstructor;
import ru.inurgalimov.http.adapter.RequestBodyAdapter;
import ru.inurgalimov.http.annotation.RequestBody;
import ru.inurgalimov.http.annotation.RequestHeader;
import ru.inurgalimov.http.annotation.RequestParam;
import ru.inurgalimov.http.request.HttpRequest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class ArgumentResolverImpl implements ArgumentResolver {

    private final RequestBodyAdapter requestBodyAdapter;
    private final List<Function<HttpRequest, Object>> functions;

    @Override
    public List<?> getArguments(Method method, HttpRequest request) {
        List<Object> objects = new ArrayList<>();
        if (functions.isEmpty()) {
            fillFunctions(method);
        }
        functions.stream().map(function -> function.apply(request)).forEach(objects::add);
        return objects;
    }

    private void fillFunctions(java.lang.reflect.Method method) {
        int index = 0;
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(RequestHeader.class)) {
                String key = parameter.getAnnotation(RequestHeader.class).value();
                functions.add(req -> getArgumentFromHeader(req, key));
            }
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                int i = index;
                index++;
                functions.add(req -> getArgumentFromRequestParam(req, i));
            }
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                Class<?> type = parameter.getType();
                functions.add(req -> getArgumentFromRequestBody(req, type));
            }
        }
    }

    private Object getArgumentFromHeader(HttpRequest request, String key) {
        return request.getHeaders().get(key);
    }

    private Object getArgumentFromRequestParam(HttpRequest request, Integer index) {
        String subUri = request.getUri().substring(request.getUri().lastIndexOf("?"));
        return subUri.split("&")[index].split("=")[1].trim();
    }

    private Object getArgumentFromRequestBody(HttpRequest request, Class<?> type) {
        return requestBodyAdapter.bytesToObject(request.getBody(), type);
    }

}
