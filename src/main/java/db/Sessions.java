package db;

import model.Session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Sessions {
    private static final ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<>();

    public static void addSession(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    public static Session getSessionById(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
