package kudos.repositories;

import kudos.model.Relation;
import kudos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RelationRepository extends MongoRepository<Relation,String>  {

    Page<Relation> findRelationsByFollowerOrderByAddedDateDesc(User follower, Pageable pageable);
    Page<Relation> findRelationsByUserToFollowOrderByAddedDateDesc(User userToFollow, Pageable pageable);
    Optional<Relation> findRelationByFollowerAndUserToFollow(User follower, User userToFollow);

}
