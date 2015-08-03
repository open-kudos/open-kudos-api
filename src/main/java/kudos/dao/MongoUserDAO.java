package kudos.dao;

import com.google.common.base.Optional;
import kudos.model.User;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chc on 15.7.27.
 */
@Repository
public class MongoUserDAO implements UserDAO{

    private Map<String,User> users = new HashMap<String,User>();

    {
        create(new User("user", "user", "user", "user"));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        if (users.containsKey(email)) {
            return Optional.of(users.get(email));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public User create(User user) {
        Optional<User> storedUser = getUserByEmail(user.getEmail());
        if (storedUser.isPresent()) {
            throw new IllegalStateException("email.occupied.email");
        }

        user.setEncryptedPassword(new StrongPasswordEncryptor().encryptPassword(user.getPassword()));

        users.put(user.getEmail(), user);

        return user;
    }

    @Override
    public User update(User user) {
        Optional<User> storedUser = getUserByEmail(user.getEmail());
        if (!storedUser.isPresent()) {
            throw new IllegalStateException("User does not exist");
        }

        users.put(user.getEmail(), user);

        return user;
    }

    @Override
    public void remove(String email){
        if(getUserByEmail(email).isPresent()){
            users.remove(email);
        }
    }
}
