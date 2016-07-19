package kudos.exceptions;

public class KudosExceededException extends BusinessException {
    public KudosExceededException(String error) {
        super(error);
    }
}
