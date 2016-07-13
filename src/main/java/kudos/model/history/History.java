package kudos.model.history;

import kudos.model.Transaction;

public class History {

    private String receiverEmail;
    private String receiverFullName;
    private String senderEmail;
    private String senderFullName;
    private int amount;
    private String comment;
    private String timestamp;
    private Transaction.Status status;

    public History(String receiverEmail, String receiverFullName, String senderEmail, String senderFullName, int amount, String comment, String timestamp, Transaction.Status status) {
        this.receiverEmail = receiverEmail;
        this.receiverFullName = receiverFullName;
        this.senderEmail = senderEmail;
        this.senderFullName = senderFullName;
        this.amount = amount;
        this.comment = comment;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverFullName() {
        return receiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Transaction.Status getStatus() {
        return status;
    }

    public void setStatus(Transaction.Status status) {
        this.status = status;
    }
}
