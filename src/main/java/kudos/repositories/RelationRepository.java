package kudos.repositories;

import kudos.model.Relation;
import kudos.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by chc on 15.8.20.
 */
public interface RelationRepository extends MongoRepository<Relation,String>  {

    List<Relation> getRelationsByFollower(User follower);
    List<Relation> getRelationsByCurrentUser(User currentUser);
    Relation getRelationByFollowerAndCurrentUser(User follower, User currentUser);

}
