package webserver.controllers;

import service.PostService;
import service.SessionService;
import service.UserService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.enums.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

public interface Controller {

    UserService userService = UserService.getInstance();
    SessionService sessionService = SessionService.getInstance();
    PostService postService = PostService.getInstance();


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
                .status(HttpResponseStatus.FOUND)
                .redirect(redirect)
                .build();
    }

    default HttpResponse createOkResponse(HttpRequest request, String filePath, Map<String, String> attr) {
        HttpResponse.Builder builder = HttpResponse.newBuilder();
        return builder.version(request.version())
                .status(HttpResponseStatus.OK)
                .fileName(filePath)
                .setAttribute(attr)
                .build();
    }

}
