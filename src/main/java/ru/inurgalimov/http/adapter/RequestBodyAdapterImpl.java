package ru.inurgalimov.http.adapter;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestBodyAdapterImpl implements RequestBodyAdapter {

    private final Gson gson;

    @Override
    public <T> T bytesToObject(byte[] bytes, Class<T> type) {
        return gson.fromJson(new String(bytes), type);
    }

}
