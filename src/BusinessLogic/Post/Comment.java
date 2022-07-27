package BusinessLogic.Post;

import Exceptions.LogicException.PostLengthException;

public class Comment extends Post {

    private final String replied_to; // id of post or comment whom this comment is replied to

    public Comment(String content, String id, String username, String replied_to, int likes, int number_of_replies)
            throws PostLengthException {
        super(content, id, username, likes, number_of_replies);
        this.replied_to = replied_to;
    }

    public String getRepliedTo() {
        return replied_to;
    }
}
