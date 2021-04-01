package ru.inurgalimov.http.adapter;

public interface RequestBodyAdapter {

    <T> T bytesToObject(byte[] bytes, Class<T> type);

}
