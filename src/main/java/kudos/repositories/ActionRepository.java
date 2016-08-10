package kudos.repositories;

import kudos.model.Action;
import kudos.model.Relation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionRepository extends MongoRepository<Action, String> {

    long deleteByRelation(Relation relation);

}
