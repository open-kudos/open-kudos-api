package kudos.web.beans.response;

public class CommentResponse extends Response {

    private String creatorId;
    private String creatorFullName;
    private String text;
    private String date;


    public CommentResponse(String creatorId, String creatorFullName, String text, String date) {
        this.creatorId = creatorId;
        this.creatorFullName = creatorFullName;
        this.text = text;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
