package kudos.exceptions;

public class IdNotSpecifiedException extends Exception {

    private String errorCause;

    public IdNotSpecifiedException(String errorCause){
        this.errorCause = errorCause;
    }

    public String getErrorCause(){
        return this.errorCause;
    }

}
