package webserver.controllers;

import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controllers.annotations.RequestMethod;
import webserver.controllers.annotations.RequestPath;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static service.SessionService.getSession;
import static webserver.http.Cookie.isValidCookie;

@RequestPath(path = "/qna/form.html")
public class QnAFormController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(QnAFormController.class);

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        if (!isValidCookie(request.cookie())) {
            return createFoundResponse(request, "/user/login.html");
        }

        Map<String, String> attributes = new HashMap<>();
        String fileName = "src/main/resources/templates/qna/form.html";
        Session userSession = getSession(request.cookie().getSessionId());
        User user = userSession.getUser();

        attributes.put("${user}", "<li style=\"pointer-events: none;\" ><a>" + userSession.getUser().getName() + " 님</a></li>");

        return createOkResponse(request, fileName, attributes);
    }

}
