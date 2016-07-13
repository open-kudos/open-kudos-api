package kudos.repositories;

import kudos.model.User;

import java.util.List;

public interface CustomUserRepository {
    List<User> searchAllFields(String filter);
}
