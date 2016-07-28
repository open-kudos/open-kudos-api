package kudos.web.beans.response;

public class CommentResponse extends Response {

    private String creatorId;
    private String creatorFullName;
    private String text;


    public CommentResponse(String creatorId, String creatorFullName, String text) {
        this.creatorId = creatorId;
        this.creatorFullName = creatorFullName;
        this.text = text;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
