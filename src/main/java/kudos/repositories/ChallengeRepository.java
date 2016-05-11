package kudos.repositories;

import kudos.model.Challenge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chc on 15.8.7.
 */
public interface ChallengeRepository extends MongoRepository<Challenge,String> {

    List<Challenge> findChallengesByCreator(String creator);

    List<Challenge> findChallengesByParticipant(String participant);

    List<Challenge> findAllChallengesByReferee(String referee);

    List<Challenge> findAllChallengesByRefereeAndStatus(String referee, Challenge.Status status);

    Challenge findChallengeById(String id);

    List<Challenge> findAllChallengesByStatus(Challenge.Status status);

    List<Challenge> findAllChallengesByParticipantAndStatus(String participant, Challenge.Status status);

    List<Challenge> findAllChallengesByParticipantAndStatus(String participant, Challenge.Status status, Pageable pageable);

    List<Challenge> findAllChallengesByCreatorAndStatus(String creator, Challenge.Status status);

}
