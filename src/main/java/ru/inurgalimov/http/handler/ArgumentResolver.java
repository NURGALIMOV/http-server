package ru.inurgalimov.http.handler;

import ru.inurgalimov.http.request.HttpRequest;

import java.lang.reflect.Method;
import java.util.List;

public interface ArgumentResolver {

    List<?> getArguments(Method method, HttpRequest request);

}
