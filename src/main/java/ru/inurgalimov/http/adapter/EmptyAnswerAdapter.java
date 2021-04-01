package ru.inurgalimov.http.adapter;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class EmptyAnswerAdapter implements AnswerAdapter<Object>{

    @Override
    public byte[] objectToBytes(Object object) {
        return new byte[0];
    }

    @Override
    public Class<Object> getHandleType() {
        return null;
    }

    @Override
    public Map<String, String> getHeader() {
        return Collections.emptyMap();
    }
}
