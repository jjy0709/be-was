package webserver.controllers;

import model.Post;
import model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controllers.annotations.RequestMethod;
import webserver.controllers.annotations.RequestPath;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static db.Posts.findAll;
import static service.SessionService.getSession;
import static webserver.http.Cookie.isValidCookie;

@RequestPath(path = "/index.html")
public class HomeController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private static final Map<String, String> requestAttribute = new HashMap<>() {{
        put("${logout}", "style=\"display:none;\"");
        put("${login}", "");
        put("${user}", "");
    }};

    @RequestMethod(method = "GET")
    public HttpResponse handleGet(HttpRequest request) {
        Map<String, String> attributes = setRequestAttribute(request);
        addPostTags(attributes);

        return createOkResponse(request, "src/main/resources/templates/index.html", attributes);
    }

    private void addPostTags(Map<String, String> attributes) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Post> postList = (List<Post>) findAll();
        for(int index = postList.size()-1; index > -1; index--) {
            Post post = postList.get(index);
            stringBuilder.append("<li>\n");
            stringBuilder.append("<div class=\"wrap\">\n");
            stringBuilder.append("<div class=\"main\">\n");
            stringBuilder.append("<strong class=\"subject\">\n");
            stringBuilder.append("<a href=\"./qna/show.html\">" + post.getTitle() + "</a>\n");
            stringBuilder.append("</strong>\n");
            stringBuilder.append("<div class=\"auth-info\">\n");
            stringBuilder.append("<i class=\"icon-add-comment\"></i>\n");
            stringBuilder.append("<span class=\"time\">" + post.getTimeString() + "</span>\n");
            stringBuilder.append("<a class=\"author\">" + post.getUser().getName() +"</a>\n");
            stringBuilder.append("</div>\n");
            stringBuilder.append("<div class=\"reply\" title=\"댓글\">\n");
            stringBuilder.append("<i class=\"icon-reply\"></i>\n");
            stringBuilder.append("<span class=\"point\">0</span>\n");
            stringBuilder.append("</div>\n");
            stringBuilder.append("</div>\n");
            stringBuilder.append("</div>\n");
            stringBuilder.append("</li>");
        }
        attributes.put("${postList}", stringBuilder.toString());
    }

    private Map<String, String> setRequestAttribute(HttpRequest request) {
        if (!isValidCookie(request.cookie())) {
            return requestAttribute;
        }

        Session session = getSession(request.cookie().getSessionId());
        Map<String, String> userAttribute = new HashMap<>();

        userAttribute.put("${logout}", "");
        userAttribute.put("${login}", "style=\"display:none;\"");
        userAttribute.put("${user}", "<li style=\"pointer-events: none;\" ><a>" + session.getUser().getName() + " 님</a></li>");

        return userAttribute;
    }
}
