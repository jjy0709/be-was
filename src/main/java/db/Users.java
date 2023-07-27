package db;

import model.User;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Users {
    private static final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();

    static {
        // 테스트를 용이하게 하기 위한 default 유저
        users.put("user", new User("user", "pass", "test", "test@gmail.com"));
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
