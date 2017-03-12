package kudos.model;


import kudos.model.status.UserStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class User {
    @Id
    @Indexed(unique = true)
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String emailHash;
    private UserStatus status;
    private int totalKudos;
    private int weeklyKudos;
    private int spendableKudos;

    private int level;
    private int experiencePoints;
    private int previousLevelExperiencePoints;
    private int experiencePointsToLevelUp;

    private List<Endorsement> endorsements;
    private List<Achievement> achievements;

    public User(String firstName, String lastName, String password, String email, UserStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public int getTotalKudos() {
        return totalKudos;
    }

    public void setTotalKudos(int totalKudos) {
        this.totalKudos = totalKudos;
    }

    public int getWeeklyKudos() {
        return weeklyKudos;
    }

    public void setWeeklyKudos(int weeklyKudos) {
        this.weeklyKudos = weeklyKudos;
    }

    public int getSpendableKudos() {
        return spendableKudos;
    }

    public void setSpendableKudos(int spendableKudos) {
        this.spendableKudos = spendableKudos;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public int getPreviousLevelExperiencePoints() {
        return previousLevelExperiencePoints;
    }

    public void setPreviousLevelExperiencePoints(int previousLevelExperiencePoints) {
        this.previousLevelExperiencePoints = previousLevelExperiencePoints;
    }

    public int getExperiencePointsToLevelUp() {
        return experiencePointsToLevelUp;
    }

    public void setExperiencePointsToLevelUp(int experiencePointsToLevelUp) {
        this.experiencePointsToLevelUp = experiencePointsToLevelUp;
    }

    public List<Endorsement> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(List<Endorsement> endorsements) {
        this.endorsements = endorsements;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }
}
