package ru.inurgalimov.http.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.inurgalimov.http.utils.HttpStatus;
import ru.inurgalimov.http.utils.HttpVersion;

import java.util.Map;

@Getter
@Setter
@Builder
public class HttpResponse {

    private HttpVersion version;
    private HttpStatus status;
    private Map<String, String> headers;
    private byte[] body;

}
