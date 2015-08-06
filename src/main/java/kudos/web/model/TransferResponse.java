package kudos.web.model;

/**
 * Created by chc on 15.8.5.
 */
public class TransferResponse extends Response {

    private String status;
    private String reason;

    public TransferResponse(String status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Response success(String reason){
        return new TransferResponse("Transfer successful",reason);
    }

    public static Response fail(String message){
        return new TransferResponse("Transfer failed", message);
    }

}
