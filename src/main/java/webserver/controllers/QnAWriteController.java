package webserver.controllers;

import model.Post;
import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controllers.annotations.RequestMethod;
import webserver.controllers.annotations.RequestPath;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.Map;
import java.util.Set;

import static db.Posts.addPost;
import static service.SessionService.getSession;
import static webserver.http.Cookie.isValidCookie;
import static webserver.http.enums.HttpResponseStatus.BAD_REQUEST;

@RequestPath(path = "/qna/write")
public class QnAWriteController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(QnAWriteController.class);

    @RequestMethod(method = "POST")
    public HttpResponse handlePost(HttpRequest request) {
        if (!isValidCookie(request.cookie())) {
            return createFoundResponse(request, "/user/login.html");
        }
        Map<String, String> parameters = request.body();

        if (!verifyParameter(parameters)) {
            // todo: 에러 상황에 맞게 페이지 만들어서 반환
            return createErrorResponse(request, BAD_REQUEST);
        }

        if (parameters.containsValue("")) {
            return createFoundResponse(request, "/qna/form_failed.html");
        }

        Session session = getSession(request.cookie().getSessionId());
        User user = session.getUser();

        Post post = new Post(user, parameters.get("title"), parameters.get("contents"));
        addPost(post);

        return createFoundResponse(request, "/index.html");
    }

    private boolean verifyParameter(Map<String, String> parameters) {
        Set<String> essentialField = Set.of("title", "contents");
        if (!parameters.keySet().equals(essentialField)) {
            return false;
        }
        return true;
    }
}
