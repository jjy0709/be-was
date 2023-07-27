package db;

import model.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Posts {
    private static List<Post> posts = new ArrayList<>();

    public static void addPost(Post post) {
        posts.add(post);
    }

    public static Post getPost(int index) {
        if (index < 0 || index - 1 > posts.size())
            return null;
        return posts.get(index);
    }

    public static Collection<Post> findAll() {
        return List.copyOf(posts);
    }
}
