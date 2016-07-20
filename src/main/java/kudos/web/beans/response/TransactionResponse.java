package kudos.web.beans.response;

import kudos.model.Transaction;

public class TransactionResponse extends Response {
    int amount;
    String message;
    String receiverEmail;
    String receiverFullName;
    String senderEmail;
    String senderFullName;
    String timestamp;

    public TransactionResponse(Transaction transaction) {
        this.amount = transaction.getAmount();
        this.message = transaction.getMessage();
        this.receiverEmail = transaction.getReceiver().getEmail();
        this.receiverFullName = transaction.getReceiver().getFirstName() + " " + transaction.getReceiver().getLastName();
        this.senderEmail = transaction.getSender().getEmail();
        this.senderFullName = transaction.getSender().getFirstName() + " " + transaction.getSender().getLastName();
        this.timestamp = transaction.getTimestamp();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public void setSenderEmail(String senderEmai) {
        this.senderEmail = senderEmai;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
