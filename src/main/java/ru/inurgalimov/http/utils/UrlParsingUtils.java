package ru.inurgalimov.http.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class UrlParsingUtils {

    public String getPath(String url) {
        if(Objects.isNull(url) || url.isBlank()) {
            throw new IllegalArgumentException("Url is null or empty");
        }
        int index = url.lastIndexOf("?");
        if (index != -1) {
            return url.substring(0, url.lastIndexOf("?"));
        }
        return url.replace("//", "/");
    }

}
