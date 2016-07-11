package kudos.model;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@ApiObject
@Document
public class Challenge {

    public enum Status {
        CREATED, ACCEPTED, DECLINED, ACCOMPLISHED, CANCELED, EXPIRED
    }

    @Id
    private String id;

    private String creator;
    private String participant;

    @DBRef
    private User creatorUser;
    @DBRef
    private User participantUser;

    @ApiObjectField
    private Boolean creatorStatus;
    @ApiObjectField
    private Boolean participantStatus;

    @ApiObjectField
    private String name;
    @ApiObjectField
    private String description;

    @ApiObjectField
    private String createDate;
    @ApiObjectField
    private String finishDate;
    @ApiObjectField
    private int amount;
    @ApiObjectField
    private Status status;


    public Challenge(User creatorUser, User participantUser, String name, String description, String createDate, String finishDate, int amount, Status status) {
        this.creatorUser = creatorUser;
        this.participantUser = participantUser;

        this.creatorStatus = null;
        this.participantStatus = null;

        this.name = name;
        this.description = description;

        this.createDate = createDate;
        this.finishDate = finishDate;
        this.amount = amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public User getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(User creatorUser) {
        this.creatorUser = creatorUser;
    }

    public User getParticipantUser() {
        return participantUser;
    }

    public void setParticipantUser(User participantUser) {
        this.participantUser = participantUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDateDate() {
        return createDate;
    }

    public void setCreateDateDate(String dueDate) {
        this.createDate = dueDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCreatorStatus(Boolean creatorStatus) {
        this.creatorStatus = creatorStatus;
    }

    public void setParticipantStatus(Boolean participantStatus) {
        this.participantStatus = participantStatus;
    }

    public Boolean getCreatorStatus() {
        return creatorStatus;
    }

    public Boolean getParticipantStatus() {
        return participantStatus;
    }
}
