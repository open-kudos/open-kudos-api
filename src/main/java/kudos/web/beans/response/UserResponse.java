package kudos.web.beans.response;

import kudos.model.User;

/**
 * Created by vytautassugintas on 11/07/16.
 */
public class UserResponse extends Response {

    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;

    protected String birthday;
    protected String phone;

    protected boolean subscribing;
    protected String lastSeenTransactionTimestamp;

    public UserResponse(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.phone = user.getPhone();
        this.subscribing = user.isSubscribing();
        this.lastSeenTransactionTimestamp = user.getLastSeenTransactionTimestamp();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSubscribing() {
        return subscribing;
    }

    public void setSubscribing(boolean subscribing) {
        this.subscribing = subscribing;
    }

    public String getLastSeenTransactionTimestamp() {
        return lastSeenTransactionTimestamp;
    }

    public void setLastSeenTransactionTimestamp(String lastSeenTransactionTimestamp) {
        this.lastSeenTransactionTimestamp = lastSeenTransactionTimestamp;
    }
}
