package kudos.web.beans.request;

public class GiveChallengeForm {

    private String receiverEmail;
    private String name;
    private String description;
    private String expirationDate;
    private Integer amount;

    public GiveChallengeForm() {}

    public GiveChallengeForm(String receiverEmail, String name, String description, Integer amount, String expirationDate) {
        this.receiverEmail = receiverEmail;
        this.name = name;
        this.description = description;
        this.expirationDate = expirationDate;
        this.amount = amount;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
