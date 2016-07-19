package kudos.repositories;

import kudos.model.Challenge;
import kudos.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChallengeRepository extends MongoRepository<Challenge,String> {

    Challenge findChallengeById(String id);

    List<Challenge> findChallengesByCreatorUser(User creator);

    List<Challenge> findChallengesByParticipantUser(User participant);

    List<Challenge> findAllChallengesByStatus(Challenge.Status status);

    List<Challenge> findAllChallengesByParticipantUserAndStatus(User participant, Challenge.Status status);

    List<Challenge> findAllChallengesByParticipantUserAndStatus(User participant, Challenge.Status status, Pageable pageable);

    List<Challenge> findAllChallengesByCreatorUserAndStatus(User creator, Challenge.Status status);

}
