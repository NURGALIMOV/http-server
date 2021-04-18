package ru.inurgalimov.http.handler;

import org.springframework.stereotype.Component;
import ru.inurgalimov.http.exception.BadHeaderException;
import ru.inurgalimov.http.exception.EmptyRequestException;
import ru.inurgalimov.http.exception.MalFormedRequestException;
import ru.inurgalimov.http.request.HttpRequest;
import ru.inurgalimov.http.utils.HttpVersion;
import ru.inurgalimov.http.utils.Method;
import ru.inurgalimov.http.utils.guava.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static ru.inurgalimov.http.utils.Method.POST;

@Component
public class RequestHandlerImpl implements RequestHandler {

    private static final byte[] END_OF_LINE = new byte[]{'\r', '\n'};
    private static final byte[] LINE_BREAK = new byte[]{'\r', '\n', '\r', '\n'};

    @Override
    public HttpRequest handleRequest(InputStream input) throws IOException {
        byte[] buffer = new byte[4096];
        input.mark(4096);
        if (input.read(buffer) > 0) {
            int requestLineIndex = Bytes.indexOf(buffer, END_OF_LINE, 0) + END_OF_LINE.length;
            String[] requestLineParts = getRequestLineParts(buffer, requestLineIndex);
            Method method = Method.valueOfByString(requestLineParts[0])
                    .orElseThrow(() -> new BadHeaderException("Invalid header: " + requestLineParts[0]));
            String uri = requestLineParts[1];
            HttpVersion version = HttpVersion.valueOfByString(requestLineParts[2]);

            int endHeaderIndex = Bytes.indexOf(buffer, LINE_BREAK, 0);
            Map<String, String> headers = getHeaders(buffer, requestLineIndex, endHeaderIndex);

            int contentLength = Optional.ofNullable(headers.get("Content-Length"))
                    .map(Integer::parseInt)
                    .orElse(0);
            byte[] body = new byte[0];
            if (method.equals(POST)) {
                body = getBody(buffer, input, contentLength, endHeaderIndex + LINE_BREAK.length);
            }
            return HttpRequest.builder()
                    .method(method)
                    .uri(uri)
                    .version(version)
                    .headers(headers)
                    .body(body)
                    .build();
        }
        throw new EmptyRequestException("Invalid request");
    }

    private String[] getRequestLineParts(byte[] source, int index) {
        if (index == -1) {
            throw new MalFormedRequestException();
        }
        String[] result = new String(source, 0, index).trim().split(" ");
        if (result.length != 3) {
            throw new BadHeaderException("Invalid request line");
        }
        return result;
    }

    private Map<String, String> getHeaders(byte[] source, int startHeaderIndex, int endHeaderIndex) {
        Map<String, String> result = new HashMap<>();
        int lastIndex = startHeaderIndex;
        int currentIndex = 0;
        do {
            currentIndex = Bytes.indexOf(source, END_OF_LINE, lastIndex) + END_OF_LINE.length;
            if (currentIndex == -1) {
                throw new MalFormedRequestException();
            }
            String headerLine = new String(source, lastIndex, currentIndex - lastIndex);
            List<String> headerParts = Arrays.stream(headerLine.split(":"))
                    .map(String::trim)
                    .collect(Collectors.toList());
            if (headerParts.size() != 2) {
                if ("Host".equals(headerParts.get(0))) {
                    result.put(headerParts.get(0), String.format("%s:%s", headerParts.get(1), headerParts.get(2)));
                    lastIndex = currentIndex;
                    continue;
                }
                throw new BadHeaderException("bad header line: " + headerLine);
            }
            result.put(headerParts.get(0), headerParts.get(1));
            lastIndex = currentIndex;
        } while (currentIndex < endHeaderIndex);
        return result;
    }

    private byte[] getBody(byte[] source, InputStream input, int contentLength, int endHeaderIndex) throws IOException {
        byte[] array = source;
        int totalSize = contentLength + endHeaderIndex;
        if (source.length < totalSize) {
            input.reset();
            array = input.readNBytes(totalSize);
        }
        return new String(array, endHeaderIndex, contentLength).getBytes();
    }

}
