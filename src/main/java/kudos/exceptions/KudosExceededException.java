package kudos.exceptions;

/**
 * Created by chc on 15.8.11.
 */
public class KudosExceededException extends BusinessException {
    public KudosExceededException(String error) {
        super(error);
    }
}
