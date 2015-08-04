package kudos.web.model;

import kudos.model.User;

/**
 * Created by chc on 15.7.30.
 */
public class DataResponse extends Response {
    private final String status;
    private final String message;
    private final String field;
    private final String cause;

    public DataResponse(String status, String message, String field, String cause) {
        this.status = status;
        this.message = message;
        this.field = field;
        this.cause = cause;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    public String getCause() {
        return cause;
    }

    public static DataResponse fail(String message) {
        return new DataResponse("fail", message, null, null);
    }
    public static DataResponse success() {
        return new DataResponse("success", "", null, null);
    }

    public static Response fail(String message, String type, String cause) {
        return new DataResponse("fail", message, type, cause);
    }
}
