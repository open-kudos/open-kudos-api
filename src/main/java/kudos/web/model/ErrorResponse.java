package kudos.web.model;

/**
 * Created by chc on 15.7.30.
 */
public class ErrorResponse extends Response {

    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
