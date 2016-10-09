package kudos.exceptions;

public class UserException extends Exception {

    private String errorCause;

    public UserException(String errorCause){
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }

}
