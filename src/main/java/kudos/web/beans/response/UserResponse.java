package kudos.web.beans.response;

import kudos.model.User;
import kudos.model.status.UserStatus;

public class UserResponse extends Response {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String startedToWorkDate;
    private boolean isCompleted;
    private boolean canFollow;
    private String followingSince;
    private int totalKudos;
    private int weeklyKudos;
    private int level;
    private int experiencePoints;
    private int experiencePointsToLevelUp;
    private int previousLevelExperiencePoints;

    public UserResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.totalKudos = user.getTotalKudos();
        this.weeklyKudos = user.getWeeklyKudos();
        this.isCompleted = user.getStatus().equals(UserStatus.COMPLETED);
        this.level = user.getLevel();
        this.experiencePoints = user.getExperiencePoints();
        this.experiencePointsToLevelUp = user.getExperiencePointsToLevelUp();
        this.previousLevelExperiencePoints = user.getPreviousLevelExperiencePoints();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStartedToWorkDate() {
        return startedToWorkDate;
    }

    public void setStartedToWorkDate(String startedToWorkDate) {
        this.startedToWorkDate = startedToWorkDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCanFollow() {
        return canFollow;
    }

    public void setCanFollow(boolean canFollow) {
        this.canFollow = canFollow;
    }

    public String getFollowingSince() {
        return followingSince;
    }

    public void setFollowingSince(String followingSince) {
        this.followingSince = followingSince;
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

    public int getExperiencePointsToLevelUp() {
        return experiencePointsToLevelUp;
    }

    public void setExperiencePointsToLevelUp(int experiencePointsToLevelUp) {
        this.experiencePointsToLevelUp = experiencePointsToLevelUp;
    }

    public int getPreviousLevelExperiencePoints() {
        return previousLevelExperiencePoints;
    }

    public void setPreviousLevelExperiencePoints(int previousLevelExperiencePoints) {
        this.previousLevelExperiencePoints = previousLevelExperiencePoints;
    }
}
