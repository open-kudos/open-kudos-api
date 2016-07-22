package kudos.web.beans.response;

import kudos.model.Challenge;
import kudos.model.ChallengeStatus;

public class ChallengeResponse extends Response {

    int amount;
    String id;
    String name;
    String createDate;
    String creatorName;
    String creatorEmail;
    Boolean creatorStatus;
    String participantName;
    String participantEmail;
    Boolean participantStatus;
    String description;
    String finishDate;
    ChallengeStatus status;

    public ChallengeResponse(Challenge challenge){
        this.id = challenge.getId();
        this.name = challenge.getName();
        this.amount = challenge.getAmount();
        this.creatorName = challenge.getCreator().getFirstName() + " " + challenge.getCreator().getLastName();
        this.creatorEmail = challenge.getCreator().getEmail();
        this.participantName = challenge.getParticipant().getFirstName() + " " + challenge.getParticipant().getLastName();
        this.participantEmail = challenge.getParticipant().getEmail();
        this.description = challenge.getDescription();
        this.finishDate = challenge.getExpirationDate();
        this.status = challenge.getStatus();
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
