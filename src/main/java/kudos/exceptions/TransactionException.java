package kudos.exceptions;

/**
 * Created by vytautassugintas on 02/05/16.
 */
public class TransactionException extends Exception{

    private String errorCause;

    public String getErrorCause(){
        return this.errorCause;
    }
}




