package ru.inurgalimov.http.handler;

import org.springframework.stereotype.Component;
import ru.inurgalimov.http.response.HttpResponse;
import ru.inurgalimov.http.utils.HttpStatus;
import ru.inurgalimov.http.utils.HttpVersion;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

@Component
public class ResponseHandlerImpl implements ResponseHandler {

    private static final String END_OF_LINE = "\r\n";

    @Override
    public void handleResponse(HttpResponse response, OutputStream output) throws IOException {
        StringBuilder answer = new StringBuilder();
        answer.append(getRequestLine(response));
        answer.append(getHeaders(response));
        output.write(joinByteArray(answer.toString().getBytes(), response.getBody()));
        output.flush();
    }

    private String getRequestLine(HttpResponse response) {
        if (Objects.isNull(response)) {
            response = HttpResponse.builder().build();
        }
        if (Objects.isNull(response.getVersion())) {
            response.setVersion(HttpVersion.HTTP_VERSION_11);
        }
        if (Objects.isNull(response.getStatus())) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return String.format("%s %s %s%s", response.getVersion().getVersion(), response.getStatus().getCode(),
                response.getStatus().getStatusText(), END_OF_LINE);
    }

    private String getHeaders(HttpResponse response) {
        if (Objects.isNull(response) || Objects.isNull(response.getHeaders())) {
            return END_OF_LINE;
        }
        StringBuilder headers = new StringBuilder();
        response.getHeaders().forEach((k, v) -> headers.append(k).append(": ").append(v).append(END_OF_LINE));
        return headers.append(END_OF_LINE).toString();
    }

    private byte[] joinByteArray(final byte[] array1, final byte[] array2) {
        if (Objects.isNull(array1)) {
            return array2;
        } else if (Objects.isNull(array2)) {
            return array1;
        }
        final byte[] result = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

}

