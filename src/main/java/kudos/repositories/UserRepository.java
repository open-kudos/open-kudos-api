package kudos.repositories;

import kudos.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {

    User findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    User findById(String id);

    User findByEmail(String email);

    Optional<User> findUserByEmailHash(String emailHash);

    List<User> findUsersByIsConfirmed(Boolean isConfirmed);

    @Query(value = "{'_id': ?0}", count = true)
    Long countUsersByEmail(String email);
}
