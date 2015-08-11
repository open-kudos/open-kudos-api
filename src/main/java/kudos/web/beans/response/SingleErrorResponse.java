package kudos.web.beans.response;

/**
 * Created by chc on 15.8.11.
 */
public class SingleErrorResponse extends Response {

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    private String cause;

    public SingleErrorResponse(String cause) {
        this.cause = cause;
    }
}
