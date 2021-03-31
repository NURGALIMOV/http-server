package ru.inurgalimov.http.handler;

import ru.inurgalimov.http.response.HttpResponse;
import ru.inurgalimov.http.utils.HttpStatus;
import ru.inurgalimov.http.utils.HttpVersion;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;

public class ResponseHandlerImpl implements ResponseHandler {

    private static final String END_OF_LINE = "\r\n";
    private static final String LINE_BREAK = "\r\n\r\n";

    @Override
    public void handleResponse(HttpResponse response, OutputStream output) throws IOException {
        StringBuilder answer = new StringBuilder();
        answer.append(getRequestLine(response));
        fillCorrectContentLength(response);
        answer.append(getHeaders(response));
        output.write(joinByteArray(answer.toString().getBytes(), response.getBody()));
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
            return "";
        }
        StringBuilder headers = new StringBuilder();
        response.getHeaders().forEach((k, v) -> headers.append(k).append(": ").append(v).append(END_OF_LINE));
        return headers.append(LINE_BREAK).toString();
    }

    private void fillCorrectContentLength(HttpResponse response) {
        final var key = "Content-Length";
        if (Objects.nonNull(response.getBody())) {
            Optional.ofNullable(response.getHeaders())
                    .map(headers -> headers.get(key))
                    .ifPresentOrElse(length -> {
                        int contentLength = Integer.parseInt(length);
                        if (contentLength != response.getBody().length) {
                            response.getHeaders().put(key, String.valueOf(response.getBody().length));
                        }
                    }, () -> response.getHeaders().put(key, String.valueOf(response.getBody().length)));
        } else {
            Optional.ofNullable(response.getHeaders()).ifPresent(headers -> headers.remove(key));
        }
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

