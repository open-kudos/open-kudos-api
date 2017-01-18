package kudos.exceptions;

public class OrderException extends Exception {

    private String errorCause;

    public OrderException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause(){
        return this.errorCause;
    }
}
