package kudos.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chc on 15.8.7.
 */
@Document
public class Challenge {

    private String senderEmail;
    private String receiverEmail;

    private String judgeEmail;
    private String challengeName;
    private String estimatedDate;

    private boolean isCompleted = false;

    private int amount;

    public Challenge(String sender, String receiver, String judge, String challengeName, String estimatedDay, int amount) {
        this.senderEmail = sender;
        this.receiverEmail = receiver;
        this.judgeEmail = judge;
        this.challengeName = challengeName;
        this.estimatedDate = estimatedDay;
        this.amount = amount;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getEstimatedDate() {
        return estimatedDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getJudgeEmail() {
        return judgeEmail;
    }

}
