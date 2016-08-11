package kudos.web.beans.response.userActionResponse;

import kudos.model.Action;
import kudos.web.beans.response.ChallengeActions;
import kudos.web.beans.response.ChallengeResponse;

public class UserChallengeActionResponse extends UserAction {

    ChallengeResponse challengeResponse;

    public UserChallengeActionResponse(Action action) {
        super(action);
        this.challengeResponse = new ChallengeResponse(action.getChallenge(), getDefaultChallengeActions());
    }

    public ChallengeResponse getChallengeResponse() {
        return challengeResponse;
    }

    public void setChallengeResponse(ChallengeResponse challengeResponse) {
        this.challengeResponse = challengeResponse;
    }

    private ChallengeActions getDefaultChallengeActions(){
        return new ChallengeActions(false, false, false, false, false);
    }

}
