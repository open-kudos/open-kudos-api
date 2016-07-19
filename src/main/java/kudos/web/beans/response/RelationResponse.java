package kudos.web.beans.response;

import kudos.model.Relation;

public class RelationResponse extends Response{

    private String id;

    private String followerEmail;
    private String followerName;

    private String userEmail;
    private String userName;

    public RelationResponse(Relation relation){
        this.id = relation.getId();
        this.followerEmail = relation.getFollower().getEmail();
        this.followerName = relation.getFollower().getFirstName() + " " + relation.getFollower().getLastName();
        this.userEmail = relation.getUserToFollow().getEmail();
        this.userName = relation.getUserToFollow().getFirstName() + " " + relation.getUserToFollow().getLastName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFollowerEmail() {
        return followerEmail;
    }

    public void setFollowerEmail(String followerEmail) {
        this.followerEmail = followerEmail;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
