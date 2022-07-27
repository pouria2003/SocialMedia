package BusinessLogic.Post;

import Exceptions.LogicException.PostLengthException;

public class Post {
    protected String content;
    protected String id;
    protected String username;
    protected int likes;
    protected int number_of_replies;

    public Post(String content, String id, String username, int likes, int number_of_replies)
            throws PostLengthException {
        this.id = id;
        if(!content.isEmpty() && content.length() < 250)
            this.content = content;
        else
            throw new PostLengthException();
        this.likes = likes;
        this.number_of_replies = number_of_replies;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public int getNumberOfReplies() {
        return number_of_replies;
    }

    public void like() {
        ++likes;
    }

    public void replied() { ++number_of_replies; }

    public String getUsername() {
        return username;
    }
}
