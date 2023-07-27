package service;

import db.Users;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserService {
    private static final UserService userService = new UserService();
    private static final ConcurrentMap<String, Lock> locks = new ConcurrentHashMap<>();

    private static Lock getLock(String userId) {
        return locks.computeIfAbsent(userId, k -> new ReentrantLock());
    }

    public static UserService getInstance() {
        return userService;
    }

    public boolean saveUser(User user) {
        Lock lock = getLock(user.getUserId());

        lock.lock();

        if (Users.findUserById(user.getUserId()) != null) {
            lock.unlock();
            return false;
        }

        Users.addUser(user);

        lock.unlock();
        return true;
    }

    public User searchUserById(String userId) {
        return Users.findUserById(userId);
    }

    public List<User> searchAllUsers() {
        return new ArrayList<>(Users.findAll());
    }
}
