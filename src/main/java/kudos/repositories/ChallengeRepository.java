package kudos.repositories;

import kudos.model.Challenge;
import kudos.model.ChallengeStatus;
import kudos.model.User;
import org.springframework.data.domain.Page;
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

    Page<Challenge> findChallengesByStatusAndCreatorOrStatusAndParticipantOrderByCreatedDateDesc(ChallengeStatus status1, User creator, ChallengeStatus status2, User participant, Pageable pageable);

    Page<Challenge> findChallengesByStatusAndParticipantOrderByClosedDateDesc(ChallengeStatus status, User participant, Pageable pageable);

    Page<Challenge> findChallengesByStatusAndParticipantOrStatusAndParticipantOrderByClosedDateDesc(ChallengeStatus status1, User participant1, ChallengeStatus status2, User participant2, Pageable pageable);

    //List<Challenge> findChallengesByParticipantAndStatus(User participant, ChallengeStatus status);

}
