package kudos.exceptions;

/**
 * Created by chc on 15.8.11.
 */
public class InvalidKudosAmountException extends BusinessException {


    public InvalidKudosAmountException(String error) {
        super(error);
    }
}
