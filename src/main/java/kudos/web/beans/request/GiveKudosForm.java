package kudos.web.beans.request;

public class GiveKudosForm {

    private String receiverEmail;
    private String message;
    private Integer amount;

    public GiveKudosForm() {}

    public GiveKudosForm(String receiverEmail, String message, Integer amount) {
        this.receiverEmail = receiverEmail;
        this.message = message;
        this.amount = amount;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}


