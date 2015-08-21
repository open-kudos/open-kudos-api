package kudos.exceptions;

/**
 * Created by chc on 15.8.12.
 */
public class IdNotSpecifiedException extends Exception {

    private String errorCause;

    public IdNotSpecifiedException(String errorCause){
        this.errorCause = errorCause;
    }

    public String getErrorCause(){
        return this.errorCause;
    }

}
