package kudos.model;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chc on 15.8.7.
 */
@ApiObject
@Document
public class Challenge {

    public enum Status {
        CREATED, ACCEPTED, DECLINED, ACCOMPLISHED, FAILED
    }

    @Id
    private String id;
    @ApiObjectField
    private String creator;
    @ApiObjectField
    private String participant;
    @ApiObjectField
    private String referee;

    @ApiObjectField
    private String name;

    @ApiObjectField
    private String dueDate;
    @ApiObjectField
    private String finishDate;
    @ApiObjectField
    private int amount;
    @ApiObjectField
    private Status status;


    public Challenge(String creator, String participant, String referee, String name, String dueDate, String finishDate, int amount, Status status) {
        this.creator = creator;
        this.participant = participant;
        this.referee = referee;

        this.name = name;

        this.dueDate = dueDate;
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

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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
}
