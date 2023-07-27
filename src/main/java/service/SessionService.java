package service;

import db.Sessions;
import model.Session;

public class SessionService {
    private static final SessionService sessionService = new SessionService();

    public static SessionService getInstance() {
        return sessionService;
    }

    public void saveSession(Session session) {
        Sessions.addSession(session);
    }

    public Session searchSessionById(String sessionId) {
        return Sessions.getSessionById(sessionId);
    }

    public boolean isValidSession(String sessionId) {
        return Sessions.getSessionById(sessionId) != null;
    }

    public void deleteSession(Session session) {
        Sessions.removeSession(session.getSessionId());
    }

}
