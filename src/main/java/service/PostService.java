package service;

import db.Posts;
import model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostService {
    private static final PostService postService = new PostService();

    public static PostService getInstance() {
        return postService;
    }

    public void savePost(Post post) {
        Posts.addPost(post);
    }

    public Post searchPostByIndex(int index) {
        return Posts.getPost(index);
    }

    public List<Post> searchAllPosts() {
        return new ArrayList<>(Posts.findAll());
    }
}
