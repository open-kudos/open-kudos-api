package kudos.exceptions;

/**
 * Created by chc on 15.8.20.
 */
public class RelationException extends Exception {

    private String errorCause;

    public RelationException(String errorCause){
        this.errorCause = errorCause;
    }

    public String getErrorCause(){
        return this.errorCause;
    }

}
