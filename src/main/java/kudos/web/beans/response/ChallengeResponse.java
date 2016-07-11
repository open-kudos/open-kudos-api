package kudos.web.beans.response;

import kudos.model.Challenge;

/**
 * Created by vytautassugintas on 11/07/16.
 */
public class ChallengeResponse extends Response {

    int amount;
    String name;
    String createDate;
    String creatorName;
    String creatorEmail;
    Boolean creatorStatus;
    String participantName;
    String participantEmail;
    Boolean participantStatus;

    public ChallengeResponse(Challenge challenge){
        this.name = challenge.getName();
        this.amount = challenge.getAmount();
        this.createDate = challenge.getCreateDateDate();
        this.creatorName = challenge.getCreatorUser().getFirstName() + " " + challenge.getCreatorUser().getLastName();
        this.creatorEmail = challenge.getCreatorUser().getEmail();
        this.creatorStatus = challenge.getCreatorStatus();
        this.participantName = challenge.getParticipantUser().getFirstName() + " " + challenge.getParticipantUser().getLastName();
        this.participantEmail = challenge.getParticipantUser().getEmail();
        this.participantStatus = challenge.getParticipantStatus();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public Boolean getCreatorStatus() {
        return creatorStatus;
    }

    public void setCreatorStatus(Boolean creatorStatus) {
        this.creatorStatus = creatorStatus;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public Boolean getParticipantStatus() {
        return participantStatus;
    }

    public void setParticipantStatus(Boolean participantStatus) {
        this.participantStatus = participantStatus;
    }
}
