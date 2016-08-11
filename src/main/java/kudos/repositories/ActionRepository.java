package kudos.repositories;

import kudos.model.Action;
import kudos.model.Challenge;
import kudos.model.Relation;
import kudos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionRepository extends MongoRepository<Action, String> {

    Page<Action> findAllByUserNot(User user, Pageable pageable);

    long deleteByRelation(Relation relation);

    long deleteByChallenge(Challenge challenge);

}
