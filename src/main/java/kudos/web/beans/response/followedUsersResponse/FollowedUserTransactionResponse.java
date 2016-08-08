package kudos.web.beans.response.followedUsersResponse;

import kudos.model.Transaction;

public class FollowedUserTransactionResponse extends FollowedUsersFeed {

    private String message;
    private String receiverId;
    private String receiverFullName;
    private String senderId;
    private String senderFullName;
    private String transactionType;
    private String date;

    public FollowedUserTransactionResponse(Transaction transaction, String transactionType) {
        this.amount = transaction.getAmount();
        this.message = transaction.getMessage();
        this.receiverId = transaction.getReceiver().getId();
        this.receiverFullName = transaction.getReceiver().getFirstName() + " " + transaction.getReceiver().getLastName();
        this.senderId = transaction.getSender().getId();
        this.senderFullName = transaction.getSender().getFirstName() + " " + transaction.getSender().getLastName();
        this.transactionType = transactionType;
        this.date = transaction.getDate();
        this.type = "TRANSACTION";
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
