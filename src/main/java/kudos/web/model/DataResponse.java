package kudos.web.model;

/**
 * Created by chc on 15.7.30.
 */
public class DataResponse extends Response {

    private final String status;
    private final String message;

    private DataResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static DataResponse fail(String message) {
        return new DataResponse("fail", message);
    }
    public static DataResponse success() {
        return new DataResponse("success", "");
    }
}
