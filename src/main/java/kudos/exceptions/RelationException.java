package kudos.exceptions;

public class RelationException extends Exception {

    private String errorCause;

    public RelationException(String errorCause){
        this.errorCause = errorCause;
    }

    public String getErrorCause(){
        return this.errorCause;
    }

}
