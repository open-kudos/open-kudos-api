package kudos.web.exceptions;

/**
 * Created by chc on 15.8.12.
 */
public class UserException extends Exception {

    private String errorCause;

    public String getErrorCause() {
        return errorCause;
    }

    public UserException(String errorCause){
        this.errorCause = errorCause;
    }

}
