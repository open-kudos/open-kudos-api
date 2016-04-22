package kudos.model;

import org.jsondoc.core.annotation.ApiObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chc on 15.8.5.
 */
@ApiObject
@Document
public class Transaction {

    @Id
    private String id;
    private String senderEmail;
    private String receiverEmail;
    private String message;
    private int amount;
    private String timestamp;

    private int receiverBalance;
// WHERE timestamp > NOW-2days AND kudosType === 'KIND' GROUP BY receiver ORDER BY sum(kudosAmount) DESC LIMIT 0,1

    public Transaction(String receiverEmail, String senderEmail, int amount, String message) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.amount = amount;
        this.message = message;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date());
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiver() {
        return receiverEmail;
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

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
