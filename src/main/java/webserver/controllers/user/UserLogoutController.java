package webserver.controllers.user;

import model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controllers.Controller;
import webserver.controllers.annotations.RequestMethod;
import webserver.controllers.annotations.RequestPath;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import static webserver.http.Cookie.isValidCookie;
import static webserver.http.enums.HttpResponseStatus.FOUND;

@RequestPath(path = "/user/logout")
public class UserLogoutController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserLogoutController.class);

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        HttpResponse.Builder builder = HttpResponse.newBuilder();

        if (isValidCookie(request.cookie())) {
            Session session = sessionService.searchSessionById(request.cookie().getSessionId());
            builder.sessionId(session.getSessionId() + ";Max-Age=0");
            sessionService.deleteSession(session);
        }

        return builder.version(request.version())
                .status(FOUND)
                .redirect("/index.html")
                .build();
    }
}
