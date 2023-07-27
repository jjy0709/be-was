package model;

import static webserver.utils.StringUtils.dateFormatter;

public class Post {
    private User user;
    private String title;
    private String contents;
    private String time;

    public Post(User user, String title, String contents) {
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.time = dateFormatter.format(System.currentTimeMillis());
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getTime() {
        return time;
    }
}
