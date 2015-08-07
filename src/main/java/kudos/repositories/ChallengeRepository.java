package kudos.repositories;

import com.mongodb.Mongo;
import kudos.model.object.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chc on 15.8.7.
 */
@Repository
public interface ChallengeRepository extends MongoRepository<Challenge,String> {

    List<Challenge> findChallengesBySenderEmail(String senderEmail);

    List<Challenge> findChallengesByReceiverEmail(String receiverEmail);

    List<Challenge> findAllChallengerByJudgeEmail(String judgeEmail);
}
