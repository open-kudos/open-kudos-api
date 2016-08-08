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

    List<Challenge> findChallengesByParticipant(User participant);

    List<Challenge> findChallengesByCreator(User creator);

    Page<Challenge> findChallengesByStatusAndParticipantOrderByClosedDateDesc(ChallengeStatus status, User participant, Pageable pageable);

    Page<Challenge> findChallengesByStatusAndCreatorOrStatusAndParticipantOrderByCreatedDateDesc(ChallengeStatus status1, User creator, ChallengeStatus status2, User participant, Pageable pageable);

    Page<Challenge> findChallengesByStatusAndParticipantOrStatusAndParticipantOrderByClosedDateDesc(ChallengeStatus status1, User participant1, ChallengeStatus status2, User participant2, Pageable pageable);

}
