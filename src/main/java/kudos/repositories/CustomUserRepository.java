package kudos.repositories;

import kudos.model.User;

import java.util.List;

/**
 * Created by Modestas on 2015-09-16.
 */
public interface CustomUserRepository {
    List<User> searchAllFields(String seed);
}
