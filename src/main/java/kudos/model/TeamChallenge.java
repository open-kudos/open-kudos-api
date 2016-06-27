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
        CREATED, ACCEPTED, DECLINED, ACCOMPLISHED, EXPIRED, CANCELED
    }

    @Id
    private String id;
    @ApiObjectField
    private String name;
    @ApiObjectField
    private List<TeamMember> firstTeam;
    @ApiObjectField
    private List<TeamMember> secondTeam;
    @ApiObjectField
    private String description;
    @ApiObjectField
    private int amount;
    @ApiObjectField
    private Boolean firstTeamStatus;
    @ApiObjectField
    private Boolean secondTeamStatus;
    @ApiObjectField
    private Status status;

    public TeamChallenge(String name, List<TeamMember> firstTeam, List<TeamMember> secondTeam, String description, int amount, Status status) {
        this.name = name;
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.description = description;
        this.amount = amount;
        this.firstTeamStatus = null;
        this.secondTeamStatus = null;
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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<TeamMember> getFirstTeam() {
        return firstTeam;
    }

    public List<TeamMember> getSecondTeam() {
        return secondTeam;
    }

    public void setFirstTeam(List<TeamMember> firstTeam) {
        this.firstTeam = firstTeam;
    }

    public void setSecondTeam(List<TeamMember> secondTeam) {
        this.secondTeam = secondTeam;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getFirstTeamStatus() {
        return firstTeamStatus;
    }

    public Boolean getSecondTeamStatus() {
        return secondTeamStatus;
    }

    public void setFirstTeamStatus(Boolean firstTeamStatus) {
        this.firstTeamStatus = firstTeamStatus;
    }

    public void setSecondTeamStatus(Boolean secondTeamStatus) {
        this.secondTeamStatus = secondTeamStatus;
    }
}
