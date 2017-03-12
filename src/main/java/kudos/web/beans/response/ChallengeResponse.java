package kudos.web.beans.response;

import kudos.model.Challenge;

public class ChallengeResponse extends Response {

    private String id;
    private int amount;
    private String name;
    private String creatorFullName;
    private String creatorId;
    private String participantFullName;
    private String participantId;
    private String description;
    private String expirationDate;
    private String status;
    private ChallengeActions actions;

    public ChallengeResponse(Challenge challenge, ChallengeActions actions){
        this.id = challenge.getId();
        this.name = challenge.getName();
        this.amount = challenge.getTransaction().getAmount();
        this.creatorFullName = challenge.getCreator().getFirstName() + " " + challenge.getCreator().getLastName();
        this.creatorId = challenge.getCreator().getId();
        this.participantFullName = challenge.getParticipant().getFirstName() + " " + challenge.getParticipant().getLastName();
        this.participantId = challenge.getParticipant().getId();
        this.description = challenge.getDescription();
        this.expirationDate = challenge.getExpirationDate();
        this.status = challenge.getStatus().name();
        this.actions = actions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getParticipantFullName() {
        return participantFullName;
    }

    public void setParticipantFullName(String participantFullName) {
        this.participantFullName = participantFullName;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChallengeActions getActions() {
        return actions;
    }

    public void setActions(ChallengeActions actions) {
        this.actions = actions;
    }
}
