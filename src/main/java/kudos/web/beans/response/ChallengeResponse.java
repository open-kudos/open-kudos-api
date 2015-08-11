package kudos.web.beans.response;

import kudos.model.Challenge;

/**
 * Created by chc on 15.8.7.
 */
public class ChallengeResponse extends Response{

    private Challenge challenge;
    private String message;

    public ChallengeResponse(Challenge challenge){
        this.message = "success";
        this.challenge = challenge;
    }

    public ChallengeResponse(String message){
        this.message = message;
    }

    public static Response success(Challenge challenge){
        return new ChallengeResponse(challenge);
    }

    public static Response fail(String message){
        return new ChallengeResponse(message);
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
