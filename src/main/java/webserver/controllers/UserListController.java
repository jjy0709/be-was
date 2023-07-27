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

import static webserver.http.Cookie.isValidCookie;

@RequestPath(path = "/user/list.html")
public class UserListController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserListController.class);

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        if (!isValidCookie(request.cookie())) {
            return createFoundResponse(request, "/user/login.html");
        }

        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (User user : userService.searchAllUsers()) {
            stringBuilder.append(String.format("<tr><th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>"
                    , i++, user.getUserId(), user.getName(), user.getEmail()));
        }

        Map<String, String> attributes = new HashMap<>();
        String filePath = "src/main/resources/templates/user/list.html";
        Session userSession = sessionService.searchSessionById(request.cookie().getSessionId());

        attributes.put("${user}", "<li style=\"pointer-events: none;\" ><a>" + userSession.getUser().getName() + " 님</a></li>");
        attributes.put("${userList}", stringBuilder.toString());

        return createOkResponse(request, filePath, attributes);
    }
}
