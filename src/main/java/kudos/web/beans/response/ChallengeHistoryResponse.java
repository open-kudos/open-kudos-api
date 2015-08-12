package kudos.web.beans.response;

import kudos.model.Challenge;

import java.util.List;

/**
 * Created by chc on 15.8.7.
 */
public class ChallengeHistoryResponse extends Response {

    private List<Challenge> challenges;

    public ChallengeHistoryResponse(List<Challenge> created) {
        this.challenges = created;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }
}
