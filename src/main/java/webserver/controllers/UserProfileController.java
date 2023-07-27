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

@RequestPath(path = "/user/profile.html")
public class UserProfileController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        if (!isValidCookie(request.cookie())) {
            return createFoundResponse(request, "/user/login.html");
        }

        Map<String, String> attributes = new HashMap<>();
        String fileName = "src/main/resources/templates/user/profile.html";
        Session userSession = getSession(request.cookie().getSessionId());
        // todo: 다른 유저 프로필 보려는 요청일 경우 구현
        User user = userSession.getUser();

        attributes.put("${user}", "<li style=\"pointer-events: none;\" ><a>" + userSession.getUser().getName() + " 님</a></li>");
        attributes.put("${userName}", user.getName());
        attributes.put("${userEmail}", user.getEmail());

        return createOkResponse(request, fileName, attributes);
    }
}
