package kudos.repositories;


        import kudos.model.TeamChallenge;
        import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamChallengeRepository extends MongoRepository<TeamChallenge, String> {


    TeamChallenge findChallengeById(String id);
}
