package kudos.exceptions;

public class ChallengeException extends Exception {

    private String errorCause;

    public ChallengeException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause(){
        return this.errorCause;
    }
}
