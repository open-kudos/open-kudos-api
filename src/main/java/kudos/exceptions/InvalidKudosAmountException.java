package kudos.exceptions;

public class InvalidKudosAmountException extends BusinessException {
    public InvalidKudosAmountException(String error) {
        super(error);
    }
}
