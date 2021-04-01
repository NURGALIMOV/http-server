package ru.inurgalimov.http.processor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.inurgalimov.http.annotation.GetMapping;
import ru.inurgalimov.http.annotation.PostMapping;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.utils.Method;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ControllerBeanPostProcessor implements BeanPostProcessor {

    private final Map<Method, Map<String, Function<HttpRequest, Object>>> routMap;
    private final Map<String, Class<?>> requiredClassesMap;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Controller.class)) {
            requiredClassesMap.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        final Class<?> clazz = requiredClassesMap.get(beanName);
        if (Objects.isNull(clazz)) {
            return bean;
        }
        final var rootPath = clazz.getAnnotation(Controller.class).value();
        for (var method : clazz.getMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                String path = String.format("/%s/%s", rootPath, method.getAnnotation(GetMapping.class).value());
                routMap.get(Method.GET).put(path.replace("//", "/"), request -> invoke(bean, method, request));
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                String path = String.format("/%s/%s", rootPath, method.getAnnotation(PostMapping.class).value());
                routMap.get(Method.POST).put(path.replace("//", "/"), request -> invoke(bean, method, request));
            }
        }
        return bean;
    }

    @SneakyThrows
    private Object invoke(Object object, java.lang.reflect.Method method, HttpRequest request) {
        return method.invoke(object, request);
    }

}
