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

    private User userToFollow;

    public Relation(User follower, User userToFollow) {
        this.follower = follower;
        this.userToFollow = userToFollow;
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

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setUserToFollow(User userToFollow) {
        this.userToFollow = userToFollow;
    }
}
