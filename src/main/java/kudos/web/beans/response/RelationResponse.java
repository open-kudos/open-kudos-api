package kudos.web.beans.response;

public class RelationResponse extends Response {

    private String userId;
    private String userFullName;
    private String userEmail;

    public RelationResponse(String userId, String userFullName, String userEmail){
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
