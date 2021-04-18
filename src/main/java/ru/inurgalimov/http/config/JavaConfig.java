package ru.inurgalimov.http.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.inurgalimov.http.adapter.AnswerAdapter;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.utils.HttpMethod;

import java.util.*;
import java.util.function.Function;

@Configuration
public class JavaConfig {

    @Bean
    public Map<HttpMethod, Map<String, Function<HttpRequest, Object>>> routeMap() {
        Map result = new EnumMap<HttpMethod, Map<String, Function<HttpRequest, Object>>>(HttpMethod.class);
        Arrays.stream(HttpMethod.values()).forEach(method -> result.put(method, new HashMap<>()));
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
