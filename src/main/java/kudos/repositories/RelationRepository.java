package kudos.repositories;

import kudos.model.Relation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by chc on 15.8.20.
 */
public interface RelationRepository extends MongoRepository<Relation,String>  {

    List<Relation> getRelationsByFollowerEmail(String followerEmail);
    List<Relation> getRelationsByUserEmail(String userEmail);
    Relation getRelationByFollowerEmailAndUserEmail(String followerEmail, String userEmail);

}
