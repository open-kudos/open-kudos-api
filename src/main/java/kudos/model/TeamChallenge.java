package kudos.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@ApiObject
@Document
public class TeamChallenge {

    public enum Status {
        CREATED, ACCEPTED, DECLINED, ACCOMPLISHED, FAILED, CANCELED
    }

    @Id
    private String id;
    @ApiObjectField
    private String name;
    @ApiObjectField
    private Map<String, Boolean> firstTeam;
    @ApiObjectField
    private Map<String, Boolean> secondTeam;
    @ApiObjectField
    private String description;
    @ApiObjectField
    private String finishDate;
    @ApiObjectField
    private int amount;
    @ApiObjectField
    private Status status;

    public TeamChallenge(String name, Map<String, Boolean> firstTeam, Map<String, Boolean> secondTeam, String description, String finishDate, int amount, Status status) {
        this.name = name;
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.description = description;
        this.finishDate = finishDate;
        this.amount = amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Map<String, Boolean> getFirstTeam() {
        return firstTeam;
    }

    public Map<String, Boolean> getSecondTeam() {
        return secondTeam;
    }

    public void setFirstTeam(Map<String, Boolean> firstTeam) {
        this.firstTeam = firstTeam;
    }

    public void setSecondTeam(Map<String, Boolean> secondTeam) {
        this.secondTeam = secondTeam;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
