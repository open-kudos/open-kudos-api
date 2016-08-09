package kudos.repositories;

import kudos.model.Action;
import kudos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionRepository extends MongoRepository<Action, String> {

    Page<Action> findAllByUser(User user, Pageable pageable);

}
