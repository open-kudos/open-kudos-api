package kudos.dao;

import kudos.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chc on 15.7.27.
 */
public class UserInMemoryDAO implements UserDAO{

    private final static Map<String,User> USERS = new HashMap<String,User>();

    @Override
    public User getUserByEmail(String email) {
        return USERS.get(email);
    }

    @Override
    public User create(User user) {
        USERS.put(user.getEmail(),user);
        return USERS.get(user.getEmail());
    }

    @Override
    public User update(User user) {
        USERS.replace(user.getEmail(),USERS.get(user.getEmail()),user);
        return USERS.get(user.getEmail());
    }
}
