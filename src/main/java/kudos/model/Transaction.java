package kudos.model;

import org.jsondoc.core.annotation.ApiObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chc on 15.8.5.
 */
@ApiObject
@Document
public class Transaction {

    public enum Status {
        COMPLETED, PENDING_CHALLENGE, COMPLETED_CHALLENGE, DECLINED_CHALLENGE, CANCELED_CHALLENGE, FAILED_CHALENGE, COMPLETED_CHALLENGE_PARTICIPANT, COMPLETED_CHALLENGE_CREATOR
    }

    @Id
    private String id;
    @DBRef
    private User sender;
    @DBRef
    private User receiver;
    private String message;
    private int amount;
    private String timestamp;
    private Status status;
    private int receiverBalance;

    public Transaction(User sender, User receiver, int amount, String message, Status status) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.message = message;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date());;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getReceiverBalance() {
        return receiverBalance;
    }

    public void setReceiverBalance(int receiverBalance) {
        this.receiverBalance = receiverBalance;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
