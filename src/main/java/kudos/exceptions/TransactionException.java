package kudos.exceptions;

public class TransactionException extends Exception{

    private String errorCause;

    public String getErrorCause(){
        return this.errorCause;
    }
}




