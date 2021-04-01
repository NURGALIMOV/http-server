package ru.inurgalimov.http.adapter;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BytesAnswerAdapter implements AnswerAdapter<byte[]> {

    @Override
    public byte[] objectToBytes(Object bytes) {
        return new String((byte[]) bytes, 0, ((byte[]) bytes).length).getBytes();
    }

    @Override
    public Class<byte[]> getHandleType() {
        return byte[].class;
    }

    @Override
    public Map<String, String> getHeader() {
        return Map.of("Content-Type", "application/json");
    }

}
