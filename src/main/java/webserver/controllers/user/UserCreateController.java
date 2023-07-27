package webserver.controllers.user;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controllers.Controller;
import webserver.controllers.annotations.RequestMethod;
import webserver.controllers.annotations.RequestPath;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static webserver.http.enums.HttpResponseStatus.BAD_REQUEST;

@RequestPath(path = "/user/create")
public class UserCreateController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserCreateController.class);

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        Map<String, String> parameters = request.uri().getParameters();
        return handleRequest(request, parameters);
    }

    @RequestMethod(method = "POST")
    public HttpResponse handlePost(HttpRequest request) throws IOException {
        Map<String, String> parameters = request.body();
        return handleRequest(request, parameters);
    }

    private HttpResponse handleRequest(HttpRequest request, Map<String, String> parameters) {
        if (!verifyParameter(parameters)) {
            return createErrorResponse(request, BAD_REQUEST);
        }

        User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
        logger.info("User info: userId: {}, password: {}, name: {}, email: {}", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        String path = "/index.html";
        if (userService.saveUser(user)) {
            path = "/user/create_failed.html";
        }

        return createFoundResponse(request, path);
    }

    private boolean verifyParameter(Map<String, String> parameters) {
        Set<String> essentialField = Set.of("userId", "password", "name", "email");
        if (!parameters.keySet().equals(essentialField)) {
            return false;
        }
        return !parameters.containsValue("");
    }
}
