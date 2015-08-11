package kudos.repositories;

import kudos.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chc on 15.8.3.
 */
@Repository
public interface UserRepository extends MongoRepository<User,String> {

    User findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
    User findByEmail(String email);


    @Query(value = "{'_id': ?0}", count = true)
    Long countUsersByEmail(String email);

}
