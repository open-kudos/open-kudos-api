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

    private String followerEmail;
    private String followerName;
    private String followerSurname;

    private String userEmail;
    private String userName;
    private String userSurname;

    public Relation(String followerEmail, String followerName, String followerSurname, String userEmail, String userName, String userSurname) {
        this.followerEmail = followerEmail;
        this.followerName = followerName;
        this.followerSurname = followerSurname;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userSurname = userSurname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getFollowerEmail() {
        return followerEmail;
    }

    public String getId() {
        return id;
    }

    public String getFollowerName() {
        return followerName;
    }

    public String getFollowerSurname() {
        return followerSurname;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSurname() {
        return userSurname;
    }
}
