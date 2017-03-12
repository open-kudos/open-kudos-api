package kudos.exceptions;

public class TransactionException extends Exception{

    private String errorCause;

    public TransactionException (String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause(){
        return this.errorCause;
    }
}




