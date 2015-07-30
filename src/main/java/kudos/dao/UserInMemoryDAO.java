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
public class UserInMemoryDAO implements UserDAO{

    private static Map<String,User> USERS = new HashMap<String,User>();

    @Override
    public Optional<User> getUserByEmail(String email) {
        if (USERS.containsKey(email)) {
            return Optional.of(USERS.get(email));
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

        USERS.put(user.getEmail(),user);

        return user;
    }

    @Override
    public User update(User user) {
        Optional<User> storedUser = getUserByEmail(user.getEmail());
        if (!storedUser.isPresent()) {
            throw new IllegalStateException("User does not exist");
        }

        USERS.put(user.getEmail(), user);

        return user;
    }
}
