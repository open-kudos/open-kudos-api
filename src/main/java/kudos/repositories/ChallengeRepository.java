package kudos.repositories;

import kudos.model.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chc on 15.8.7.
 */
@Repository
public interface ChallengeRepository extends MongoRepository<Challenge,String> {

    List<Challenge> findChallengesByCreator(String creator);

    List<Challenge> findChallengesByParticipant(String participant);

    List<Challenge> findAllChallengesByReferee(String referee);

    Challenge findChallengeById(String id);

    List<Challenge> findAllChallengesByStatus(Challenge.Status status);

}
