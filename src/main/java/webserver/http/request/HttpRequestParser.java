package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static webserver.utils.StringUtils.getKeyString;
import static webserver.utils.StringUtils.getValueString;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);

    public static HttpRequest parseHttpRequest(InputStream in) throws IOException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String firstLine = bufferedReader.readLine();
        parseFirstLine(builder, firstLine);
        parseHeaders(builder, bufferedReader);

        int contentLength = builder.getContentLength();
        parseBody(builder, bufferedReader, contentLength);

        return builder.build();
    }

    private static void parseBody(HttpRequest.Builder builder, BufferedReader bufferedReader, int contentLength) throws IOException {
        if (contentLength == 0) {
            return;
        }

        char[] bodyCArray = new char[contentLength];
        bufferedReader.read(bodyCArray, 0, contentLength);

        String bodyString = new String(bodyCArray);
        String[] params = bodyString.split("&");
        for (String param : params) {
            builder.setBody(URLDecoder.decode(param, StandardCharsets.UTF_8));
        }
    }

    private static void parseHeaders(HttpRequest.Builder builder, BufferedReader bufferedReader) throws IOException {
        String headerString = bufferedReader.readLine();
        while (headerString.length() != 0) {
            int splitIndex = headerString.indexOf(":");
            builder.setHeader(getKeyString(headerString, splitIndex), getValueString(headerString, splitIndex));
            headerString = bufferedReader.readLine();
        }
    }

    private static void parseFirstLine(HttpRequest.Builder builder, String firstLine) {
        String[] tokens = firstLine.split(" ");
        builder.method(tokens[0].trim())
                .uri(tokens[1].trim())
                .version(tokens[2].trim());
    }
}
