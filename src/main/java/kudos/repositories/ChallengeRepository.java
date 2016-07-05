package kudos.repositories;

import kudos.model.Challenge;
import kudos.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by chc on 15.8.7.
 */
public interface ChallengeRepository extends MongoRepository<Challenge,String> {

    List<Challenge> findChallengesByCreator(User creator);

    List<Challenge> findChallengesByParticipant(User participant);

    Challenge findChallengeById(String id);

    List<Challenge> findAllChallengesByStatus(Challenge.Status status);

    List<Challenge> findAllChallengesByParticipantAndStatus(User participant, Challenge.Status status);

    List<Challenge> findAllChallengesByParticipantAndStatus(User participant, Challenge.Status status, Pageable pageable);

    List<Challenge> findAllChallengesByCreatorAndStatus(User creator, Challenge.Status status);

    List<Challenge> findAllChallengesByCreatorAndStatus(User creator, Challenge.Status status, Pageable pageable);

    List<Challenge> findAllChallengesByCreatorOrParticipantAndStatus(User creator, User participant, Challenge.Status status);

}
