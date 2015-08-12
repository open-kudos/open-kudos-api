package kudos.exceptions;

/**
 * Created by chc on 15.8.11.
 */
public class BusinessException extends Exception {

    private String error;

    public BusinessException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
