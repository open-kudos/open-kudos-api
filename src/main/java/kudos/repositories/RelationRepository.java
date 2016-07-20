package kudos.repositories;

import kudos.model.Relation;
import kudos.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RelationRepository extends MongoRepository<Relation,String>  {

    List<Relation> getRelationsByFollower(User follower);
    List<Relation> getRelationsByUserToFollow(User userToFollow);
    Relation getRelationByFollowerAndUserToFollow(User follower, User userToFollow);

}
