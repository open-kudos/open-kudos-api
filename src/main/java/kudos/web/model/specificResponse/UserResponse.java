package kudos.web.model.specificResponse;

import kudos.model.object.User;
import kudos.web.model.mainResponse.Response;

/**
 * Created by chc on 15.8.4.
 */
public class UserResponse extends Response {

    User user;

    public UserResponse(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public static Response showUser(User user){
        return new UserResponse(user);
    }

}
