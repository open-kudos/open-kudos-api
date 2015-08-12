package kudos.model;

import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chc on 15.8.7.
 */
@Document
public class Challenge {

    public enum Status {
        CREATED, ACCEPTED, DECLINED, ACCOMPLISHED, FAILED
    }

    @Id
    private String id;
    private String creator;
    private String participant;
    private String referee;

    private String name;

    private LocalDate dueDate;
    private LocalDate finishDate;
    private int amount;
    private Status status;


    public Challenge(String creator, String participant, String referee, String name, LocalDate dueDate, LocalDate finishDate, int amount, Status status) {
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
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
