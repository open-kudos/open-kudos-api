package kudos.web.exceptions;

public class UserException extends Exception {

    private String errorCause;

    public String getErrorCause() {
        return errorCause;
    }

    public UserException(String errorCause){
        this.errorCause = errorCause;
    }

}
