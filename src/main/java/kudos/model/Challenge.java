package kudos.model;

import org.joda.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by chc on 15.8.7.
 */
@Document
public class Challenge {

    private String senderEmail;
    private String participantEmail;

    private String refereeEmail;
    private String challengeName;
    private LocalDate dueDate;

    private boolean isCompleted = false;

    private int amount;

    public Challenge(String senderEmail, String participantEmail, String judge, String challengeName, LocalDate dueDate, int amount) {
        this.senderEmail = senderEmail;
        this.participantEmail = participantEmail;
        this.refereeEmail = judge;
        this.challengeName = challengeName;
        this.dueDate = dueDate;
        this.amount = amount;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
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

    public String getRefereeEmail() {
        return refereeEmail;
    }

}
