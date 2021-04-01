package ru.inurgalimov.http.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.inurgalimov.http.adapter.AnswerAdapter;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.utils.Method;

import java.util.*;
import java.util.function.Function;

@Configuration
public class JavaConfig {

    @Bean
    public Map<Method, Map<String, Function<HttpRequest, Object>>> routMap() {
        final var result = new EnumMap<Method, Map<String, Function<HttpRequest, Object>>>(Method.class);
        Arrays.stream(Method.values()).forEach(method -> result.put(method, new HashMap<>()));
        return result;
    }

    @Bean
    public Map<Class<?>, AnswerAdapter<?>> adapters(List<AnswerAdapter> answerAdapters) {
        Map<Class<?>, AnswerAdapter<?>> adapters = new HashMap<>();
        answerAdapters.forEach(adapter -> adapters.put(adapter.getHandleType(), adapter));
        return adapters;
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public Map<String, Class<?>> requiredClassesMap() {
        return new HashMap<>();
    }

}
