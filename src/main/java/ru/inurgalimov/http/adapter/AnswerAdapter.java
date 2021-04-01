package ru.inurgalimov.http.adapter;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface AnswerAdapter<T> {

    byte[] objectToBytes(Object object);

    Class<T> getHandleType();

    Map<String, String> getHeader();

}
