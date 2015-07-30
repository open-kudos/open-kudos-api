package kudos.web.model;

/**
 * Created by chc on 15.7.30.
 */
public class LoginResponse extends Response {

    private final String status;
    private final String message;

    private LoginResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static LoginResponse fail(String message) {
        return new LoginResponse("fail", message);
    }
    public static LoginResponse success() {
        return new LoginResponse("success", "");
    }
}
