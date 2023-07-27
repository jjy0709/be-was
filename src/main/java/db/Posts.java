package db;

import model.Post;
import model.User;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Posts {
    private static ConcurrentMap<User, Post> posts = new ConcurrentHashMap<>();

    public static void addPost(Post post) { posts.put(post.getUser(), post); }

    public static Collection<Post> findAll() { return posts.values(); }
}
