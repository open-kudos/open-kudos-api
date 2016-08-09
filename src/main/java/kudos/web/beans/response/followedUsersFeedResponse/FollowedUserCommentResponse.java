package kudos.web.beans.response.followedUsersFeedResponse;

import kudos.model.Action;
import kudos.web.beans.response.CommentResponse;

public class FollowedUserCommentResponse extends FollowedUsersFeed {

    CommentResponse commentResponse;

    public FollowedUserCommentResponse(Action action) {
        super(action);
        this.commentResponse = new CommentResponse(action.getComment());
    }

    public CommentResponse getCommentResponse() {
        return commentResponse;
    }

    public void setCommentResponse(CommentResponse commentResponse) {
        this.commentResponse = commentResponse;
    }
}
