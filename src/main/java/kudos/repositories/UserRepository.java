package kudos.repositories;

import kudos.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by chc on 15.8.3.
 */
public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {

    User findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    User findByEmail(String email);

    Optional<User> findUserByEmailHash(String emailHash);

    @Query(value = "{'_id': ?0}", count = true)
    Long countUsersByEmail(String email);
}
