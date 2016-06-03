package kudos.repositories;


import kudos.model.TeamChallenge;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TeamChallengeRepository extends MongoRepository<TeamChallenge, String> {


    TeamChallenge findChallengeById(String id);
    List<TeamChallenge> findAllChallengesByStatus(TeamChallenge.Status status);
    List<TeamChallenge> findAllChallengesByFirstTeamMemberEmailAndStatus(String memberEmail, TeamChallenge.Status status);
    List<TeamChallenge> findAllChallengesBySecondTeamMemberEmailAndStatus(String memberEmail, TeamChallenge.Status status);
}
