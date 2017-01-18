package kudos.exceptions;

public class InvalidItemAmountException extends Exception {

    private String errorCause;

    public InvalidItemAmountException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
