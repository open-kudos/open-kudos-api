package kudos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chc on 15.8.20.
 */
@Document
public class Relation {

    @Id
    private String id;

    private User follower;

    private User currentUser;

    public Relation(User follower, User currentUser) {
        this.follower = follower;
        this.currentUser = currentUser;
    }

    public User getFollower() {
        return follower;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getId() {
        return id;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
