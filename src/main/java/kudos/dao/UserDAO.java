package kudos.dao;

import kudos.model.User;
import org.springframework.stereotype.Component;

/**
 * Created by chc on 15.7.27.
 */
@Component
public interface UserDAO {

    User getUserByEmail(String email);

    User create(User user);

    User update(User user);
}
