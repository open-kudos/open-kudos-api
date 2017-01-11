package kudos.web.beans.response;

import kudos.model.Transaction;

public class KudosTransactionResponse extends Response {
    private int amount;
    private String message;
    private String endorsement;
    private String receiverId;
    private String receiverFullName;
    private String senderId;
    private String senderFullName;
    private String type;
    private String date;

    public KudosTransactionResponse(Transaction transaction, String type) {
        this.amount = transaction.getAmount();
        this.message = transaction.getMessage();
        this.endorsement = transaction.getEndorsement();
        this.receiverId = transaction.getReceiver().getId();
        this.receiverFullName = transaction.getReceiver().getFirstName() + " " + transaction.getReceiver().getLastName();
        this.senderId = transaction.getSender().getId();
        this.senderFullName = transaction.getSender().getFirstName() + " " + transaction.getSender().getLastName();
        this.type = type;
        this.date = transaction.getDate();
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

    public String getEndorsement() {
        return endorsement;
    }

    public void setEndorsement(String endorsement) {
        this.endorsement = endorsement;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverFullName() {
        return receiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
