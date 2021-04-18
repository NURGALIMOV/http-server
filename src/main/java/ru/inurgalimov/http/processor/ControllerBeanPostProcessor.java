package ru.inurgalimov.http.processor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.inurgalimov.http.adapter.RequestBodyAdapter;
import ru.inurgalimov.http.annotation.GetMapping;
import ru.inurgalimov.http.annotation.PostMapping;
import ru.inurgalimov.http.handler.ArgumentResolver;
import ru.inurgalimov.http.handler.ArgumentResolverImpl;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.utils.HttpMethod;
import ru.inurgalimov.http.utils.UrlParsingUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ControllerBeanPostProcessor implements BeanPostProcessor {

    private final Map<HttpMethod, Map<String, Function<HttpRequest, Object>>> routMap;
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
                fillRouteMap(bean, method, HttpMethod.GET, () ->
                        String.format("/%s/%s", rootPath, method.getAnnotation(GetMapping.class).value()));
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                fillRouteMap(bean, method, HttpMethod.POST, () ->
                        String.format("/%s/%s", rootPath, method.getAnnotation(PostMapping.class).value()));
            }
        }
        return bean;
    }

    private void fillRouteMap(Object bean, Method method, HttpMethod httpMethod, Supplier<String> supplier) {
        String path = supplier.get();
        path = UrlParsingUtils.getPath(path);
        ArgumentResolver resolver = new ArgumentResolverImpl(requestBodyAdapter, new ArrayList<>());
        routMap.get(httpMethod).put(path, request -> invoke(bean, method, request, resolver));
    }

    @SneakyThrows
    private Object invoke(Object object, Method method, HttpRequest request, ArgumentResolver resolver) {
        List<?> objects = resolver.getArguments(method, request);
        return method.invoke(object, objects.isEmpty() ? request : objects.toArray());
    }

}
