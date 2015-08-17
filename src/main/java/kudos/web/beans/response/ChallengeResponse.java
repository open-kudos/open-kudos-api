package kudos.web.beans.response;

import kudos.model.Challenge;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Created by chc on 15.8.7.
 */
@ApiObject
public class ChallengeResponse extends Response {

    @ApiObjectField
    private Challenge challenge;

    public ChallengeResponse(Challenge challenge) {
        this.challenge = challenge;
    }

    public static Response success(Challenge challenge) {
        return new ChallengeResponse(challenge);
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
