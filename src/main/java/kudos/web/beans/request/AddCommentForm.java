package kudos.web.beans.request;

public class AddCommentForm {
    String comment;

    public AddCommentForm() {}

    public AddCommentForm(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
