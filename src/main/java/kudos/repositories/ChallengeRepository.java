package kudos.repositories;

import kudos.model.Challenge;
import kudos.model.ChallengeStatus;
import kudos.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends MongoRepository<Challenge,String> {

    Optional<Challenge> findChallengeById(String id);
//
//    List<Challenge> findChallengesByCreatorUser(User creator);
//
//    List<Challenge> findChallengesByParticipantUser(User participant);
//
//    List<Challenge> findAllChallengesByStatus(ChallengeStatus status);
//
//    List<Challenge> findAllChallengesByParticipantUserAndStatus(User participant, ChallengeStatus status);
//
//    List<Challenge> findAllChallengesByParticipantUserAndStatus(User participant, ChallengeStatus status, Pageable pageable);
//
//    List<Challenge> findAllChallengesByCreatorUserAndStatus(User creator, ChallengeStatus status);

    List<Challenge> findChallengesByCreatorOrParticipantAndStatus(User creator, User participant, ChallengeStatus status);

    //List<Challenge> findChallengesByParticipantAndStatus(User participant, ChallengeStatus status);

}
