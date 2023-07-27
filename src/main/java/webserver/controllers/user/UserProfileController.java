package webserver.controllers.user;

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
import static webserver.utils.StringUtils.TEMPLATE_PATH;

@RequestPath(path = "/user/profile.html")
public class UserProfileController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        if (!isValidCookie(request.cookie())) {
            return createFoundResponse(request, "/user/login.html");
        }

        Map<String, String> attributes = new HashMap<>();
        String fileName = TEMPLATE_PATH.concat("/user/profile.html");
        Session userSession = sessionService.searchSessionById(request.cookie().getSessionId());
        User user = getUserInfo(userSession, request);

        attributes.put("${user}", "<li style=\"pointer-events: none;\" ><a>" + userSession.getUser().getName() + " 님</a></li>");
        attributes.put("${userName}", user.getName());
        attributes.put("${userEmail}", user.getEmail());

        return createOkResponse(request, fileName, attributes);
    }

    private User getUserInfo(Session userSession, HttpRequest request) {
        if (!request.uri().hasParameter())
            return userSession.getUser();

        String userId = request.uri().getParameter("userId");

        if (userId == null || userService.searchUserById(userId) == null)
            return new User("null", "", "존재하지 않는 유저입니다.", "");

        return userService.searchUserById(userId);
    }
}
