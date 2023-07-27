package webserver.controllers;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static webserver.http.enums.HttpResponseStatus.FOUND;
import static webserver.http.enums.HttpResponseStatus.OK;

public interface Controller {

    // TODO: Exception으로 빼서 handle?
    default HttpResponse createErrorResponse(HttpRequest request, HttpResponseStatus status) {
        HttpResponse.Builder builder = HttpResponse.newBuilder();

        Map<String, String> attr = new HashMap<>();
        attr.put("${errorCode}", String.valueOf(status.getStatusCode()));
        attr.put("${errorMessage}", status.getStatusText());

        return builder.version(request.version())
                .status(status)
                .fileName("src/main/resources/templates/error/error.html")
                .setAttribute(attr)
                .build();
    }

    default HttpResponse createFoundResponse(HttpRequest request, String redirect) {
        HttpResponse.Builder builder = HttpResponse.newBuilder();
        return builder.version(request.version())
                .status(FOUND)
                .redirect(redirect)
                .build();
    }

    default HttpResponse createOkResponse(HttpRequest request, String filePath, Map<String, String> attr) {
        HttpResponse.Builder builder = HttpResponse.newBuilder();
        return builder.version(request.version())
                .status(OK)
                .fileName(filePath)
                .setAttribute(attr)
                .build();
    }

}
