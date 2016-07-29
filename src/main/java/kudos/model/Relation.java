package kudos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Relation {

    @Id
    private String id;
    @DBRef
    private User follower;
    @DBRef
    private User userToFollow;
    private String addedDate;

    public Relation(User follower, User userToFollow, String addedDate) {
        this.follower = follower;
        this.userToFollow = userToFollow;
        this.addedDate = addedDate;
    }

    public User getFollower() {
        return follower;
    }

    public User getUserToFollow() {
        return userToFollow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setUserToFollow(User userToFollow) {
        this.userToFollow = userToFollow;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
}
