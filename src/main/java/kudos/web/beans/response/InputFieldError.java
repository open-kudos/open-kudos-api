package kudos.web.beans.response;

public class InputFieldError {
    private final String field;
    private final String cause;
    private final String message;

    public InputFieldError(String field, String cause, String message) {
        this.field = field;
        this.cause = cause;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }
}
