package kudos.dao;

import com.google.common.base.Optional;
import kudos.model.User;
import org.springframework.stereotype.Component;

/**
 * Created by chc on 15.7.27.
 */
public interface UserDAO {

    Optional<User> getUserByEmail(String email);

    User create(User user);

    User update(User user);

    void remove(String email);
}
