package kudos.web.beans.response.userActionResponse;

import kudos.model.Action;
import kudos.web.beans.response.CommentResponse;

public class UserCommentActionResponse extends UserAction {

    CommentResponse commentResponse;

    public UserCommentActionResponse(Action action) {
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
