package kudos.exceptions;

public class BusinessException extends Exception {

    private String error;

    public BusinessException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
