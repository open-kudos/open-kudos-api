package kudos.repositories;

import kudos.model.User;
import kudos.model.UserStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmailHash(String emailHash);

    List<User> findUsersByStatusNot(UserStatus status);

    List<User> findUsersByFirstNameLikeIgnoreCaseAndIdNot(String emailPredicate, String id);
}
