package kudos.web.beans.response;

import kudos.model.Challenge;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by chc on 15.8.7.
 */
public class ChallengeHistoryResponse extends Response {

    List<Challenge> challengeList;

    public List<Challenge> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    public ChallengeHistoryResponse(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }
}
