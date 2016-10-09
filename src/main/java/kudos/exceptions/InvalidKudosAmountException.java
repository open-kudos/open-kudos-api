package kudos.exceptions;

public class InvalidKudosAmountException extends Exception {

    private String errorCause;

    public InvalidKudosAmountException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
