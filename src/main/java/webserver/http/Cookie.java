package webserver.http;

import service.SessionService;

import java.util.Map;

public class Cookie {
    private Map<String, String> attributes;

    public Cookie(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getSessionId() {
        return attributes.getOrDefault("sid", null);
    }

    public static boolean isValidCookie(Cookie cookie) {
        if (cookie == null)
            return false;

        return SessionService.getInstance().isValidSession(cookie.getSessionId());
    }
}
