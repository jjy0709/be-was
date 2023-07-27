package webserver.controllers;

import model.Post;
import model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controllers.annotations.RequestMethod;
import webserver.controllers.annotations.RequestPath;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.ContentType;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static webserver.http.Cookie.isValidCookie;
import static webserver.http.enums.ContentType.HTML;
import static webserver.http.enums.ContentType.getContentTypeOfFile;
import static webserver.http.enums.HttpResponseStatus.NOT_FOUND;

@RequestPath(path = "/")
public class StaticFileController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileController.class);
    private static final Map<String, String> requestAttribute = new HashMap<>() {{
        put("${logout}", "style=\"display:none;\"");
        put("${login}", "");
        put("${user}", "");
    }};

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        String fileName = request.uri().getPath();
        ContentType contentType = getContentTypeOfFile(fileName);
        String filePath = getPathString(fileName, contentType);

        if (!Files.exists(Paths.get(filePath)))
            return createErrorResponse(request, NOT_FOUND);

        Map<String, String> attributes = setRequestAttribute(request);

        if (request.uri().getPath().equals("/index.html"))
            addPostTags(attributes);

        return createOkResponse(request, filePath, attributes);
    }

    private Map<String, String> setRequestAttribute(HttpRequest request) {
        if (!isValidCookie(request.cookie())) {
            return requestAttribute;
        }

        Session session = sessionService.searchSessionById(request.cookie().getSessionId());
        Map<String, String> userAttribute = new HashMap<>();

        userAttribute.put("${logout}", "");
        userAttribute.put("${login}", "style=\"display:none;\"");
        userAttribute.put("${user}", "<li style=\"pointer-events: none;\" ><a>" + session.getUser().getName() + " 님</a></li>");

        return userAttribute;
    }

    private String getPathString(String fileName, ContentType contentType) {
        if (contentType == HTML) {
            return "src/main/resources/templates".concat(fileName);
        }
        return "src/main/resources/static".concat(fileName);
    }

    private void addPostTags(Map<String, String> attributes) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Post> postList = postService.searchAllPosts();
        int length = postList.size();
        for (int index = length - 1; index > -1; index--) {
            Post post = postList.get(index);
            stringBuilder.append("<li>\n")
                    .append("<div class=\"wrap\">\n")
                    .append("<div class=\"main\">\n")
                    .append("<strong class=\"subject\">\n")
                    .append("<a href=\"./qna/show.html?index=" + (index) + "\">" + post.getTitle() + "</a>\n")
                    .append("</strong>\n")
                    .append("<div class=\"auth-info\">\n")
                    .append("<i class=\"icon-add-comment\"></i>\n")
                    .append("<span class=\"time\">" + post.getTimeString() + "</span>\n")
                    .append("<a href=\"./user/profile.html?userId=" + post.getUser().getUserId() + "\"class=\"author\">" + post.getUser().getName() + "</a>\n")
                    .append("</div>\n")
                    .append("<div class=\"reply\" title=\"댓글\">\n")
                    .append("<i class=\"icon-reply\"></i>\n")
                    .append("<span class=\"point\">" + (index + 1) + "</span>\n")
                    .append("</div>\n")
                    .append("</div>\n")
                    .append("</div>\n")
                    .append("</li>");
        }
        attributes.put("${postList}", stringBuilder.toString());
    }
}
