package kudos.web.beans.response;

import kudos.model.User;

public class UserResponse extends Response {

    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;

    protected String birthday;
    protected String startedToWorkDate;

    protected boolean subscribing;
    protected boolean isCompleted;
    protected String lastSeenTransactionTimestamp;

    public UserResponse(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.startedToWorkDate = user.getStartedToWorkDate();
        this.lastSeenTransactionTimestamp = user.getLastNotificationCheckTime();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isSubscribing() {
        return subscribing;
    }

    public void setSubscribing(boolean subscribing) {
        this.subscribing = subscribing;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getLastSeenTransactionTimestamp() {
        return lastSeenTransactionTimestamp;
    }

    public void setLastSeenTransactionTimestamp(String lastSeenTransactionTimestamp) {
        this.lastSeenTransactionTimestamp = lastSeenTransactionTimestamp;
    }
}
