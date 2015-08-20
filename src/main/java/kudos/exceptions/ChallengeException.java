package kudos.exceptions;

/**
 * Created by chc on 15.8.14.
 */
public class ChallengeException extends Exception {

    private String errorCause;

    public ChallengeException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause(){
        return this.errorCause;
    }
}
