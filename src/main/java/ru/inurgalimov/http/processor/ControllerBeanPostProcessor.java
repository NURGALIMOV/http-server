package ru.inurgalimov.http.processor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.inurgalimov.http.adapter.RequestBodyAdapter;
import ru.inurgalimov.http.annotation.*;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.utils.Method;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ControllerBeanPostProcessor implements BeanPostProcessor {

    private final Map<Method, Map<String, Function<HttpRequest, Object>>> routMap;
    private final Map<String, Class<?>> requiredClassesMap;
    private final RequestBodyAdapter requestBodyAdapter;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Controller.class)) {
            requiredClassesMap.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = requiredClassesMap.get(beanName);
        if (Objects.isNull(clazz)) {
            return bean;
        }
        String rootPath = clazz.getAnnotation(Controller.class).value();
        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                String path = getPath(rootPath, method, method.getAnnotation(GetMapping.class).value());
                routMap.get(Method.GET).put(path, request -> invoke(bean, method, request));
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                String path = getPath(rootPath, method, method.getAnnotation(PostMapping.class).value());
                routMap.get(Method.POST).put(path, request -> invoke(bean, method, request));
            }
        }
        return bean;
    }

    private String getPath(String rootPath, java.lang.reflect.Method method, String value) {
        String path = String.format("/%s/%s", rootPath, value);
        int index = path.lastIndexOf("?");
        if (index != -1) {
            return path.substring(0, path.lastIndexOf("?"));
        }
        return path.replace("//", "/");
    }

    @SneakyThrows
    private Object invoke(Object object, java.lang.reflect.Method method, HttpRequest request) {
        List<Object> objects = new ArrayList<>();
        int index = 0;
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(RequestHeader.class)) {
                objects.add(request.getHeaders().get(parameter.getAnnotation(RequestHeader.class).value()));
            }
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                String subUri = request.getUri().substring(request.getUri().lastIndexOf("?"));
                objects.add(subUri.split("&")[index++].split("=")[1].trim());
            }
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                objects.add(requestBodyAdapter.bytesToObject(request.getBody(), parameter.getType()));
            }
        }
        if (objects.isEmpty()) {
            return method.invoke(object, request);
        }
        return method.invoke(object, objects.toArray());
    }

}
