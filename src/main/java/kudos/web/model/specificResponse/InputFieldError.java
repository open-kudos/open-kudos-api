package kudos.web.model.specificResponse;

/**
 * Created by chc on 15.8.3.
 */
public class InputFieldError {
    private final String field;
    private final String cause;

    public InputFieldError(String field, String cause) {
        this.field = field;
        this.cause = cause;
    }

    public String getField() {
        return field;
    }

    public String getCause() {
        return cause;
    }
}
