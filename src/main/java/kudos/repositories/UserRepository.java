package kudos.repositories;

import kudos.model.User;
import kudos.model.UserStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmailHash(String emailHash);

    List<User> findUsersByStatus(UserStatus status);

    List<User> findUsersByStatusNot(UserStatus status);

    @Query(value = "{'_id': ?0}", count = true)
    Long countUsersByEmail(String email);
}
