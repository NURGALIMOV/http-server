package ru.inurgalimov.http.adapter;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ObjectAnswerAdapter implements AnswerAdapter<Object>{

    private final Gson gson;

    @Override
    public byte[] objectToBytes(Object object) {
        return gson.toJson(object).getBytes();
    }

    @Override
    public Class<Object> getHandleType() {
        return Object.class;
    }

    @Override
    public Map<String, String> getHeader() {
        return Map.of("Content-Type", "application/json");
    }

}
