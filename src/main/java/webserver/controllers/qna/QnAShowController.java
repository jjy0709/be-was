package webserver.controllers.qna;

import model.Post;
import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controllers.Controller;
import webserver.controllers.annotations.RequestMethod;
import webserver.controllers.annotations.RequestPath;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static webserver.http.Cookie.isValidCookie;
import static webserver.http.enums.HttpResponseStatus.BAD_REQUEST;
import static webserver.utils.StringUtils.TEMPLATE_PATH;

@RequestPath(path = "/qna/show.html")
public class QnAShowController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(QnAShowController.class);

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        if (!isValidCookie(request.cookie())) {
            return createFoundResponse(request, "/user/login.html");
        }

        Map<String, String> attributes = new HashMap<>();
        String fileName = TEMPLATE_PATH.concat("/qna/show.html");
        Session userSession = sessionService.searchSessionById(request.cookie().getSessionId());
        User user = userSession.getUser();

        String postIndex = request.uri().getParameter("index");
        if(postIndex == null)
            return createErrorResponse(request, BAD_REQUEST);

        Post post = postService.searchPostByIndex(Integer.parseInt(postIndex));
        if(post == null)
            return createErrorResponse(request, BAD_REQUEST);

        attributes.put("${user}", "<li style=\"pointer-events: none;\" ><a>" + userSession.getUser().getName() + " 님</a></li>");
        attributes.put("${postTitle}", post.getTitle());
        attributes.put("${postUser}", post.getUser().getName());
        attributes.put("${postTime}", post.getTimeString());

        putPostContentToAttr(attributes, post);

        return createOkResponse(request, fileName, attributes);
    }

    private void putPostContentToAttr(Map<String, String> attributes, Post post) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] lines = post.getContents().split("\n");
        for(String line: lines) {
            stringBuilder.append(String.format("<p>%s</p>", line));
        }

        attributes.put("${postContent}", stringBuilder.toString());
    }
}
