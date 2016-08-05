package kudos.web.beans.response;

import kudos.model.User;
import kudos.model.UserStatus;

public class UserResponse extends Response {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String startedToWorkDate;
    private boolean isCompleted;
    private int totalKudos;
    private int weeklyKudos;

    public UserResponse(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.startedToWorkDate = user.getStartedToWorkDate();
        this.totalKudos = user.getTotalKudos();
        this.weeklyKudos = user.getWeeklyKudos();
        if(user.getStatus().equals(UserStatus.COMPLETED)) {
            this.isCompleted = true;
        } else {
            this.isCompleted = false;
        }
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

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
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
}
