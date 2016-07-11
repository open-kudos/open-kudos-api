package kudos.web.beans.response;

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

    protected String lastSeenTransactionTimestamp;

    public UserResponse(String id, String firstName, String lastName, String email, String birthday, String phone, String lastSeenTransactionTimestamp) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.phone = phone;
        this.lastSeenTransactionTimestamp = lastSeenTransactionTimestamp;
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

    public String getLastSeenTransactionTimestamp() {
        return lastSeenTransactionTimestamp;
    }

    public void setLastSeenTransactionTimestamp(String lastSeenTransactionTimestamp) {
        this.lastSeenTransactionTimestamp = lastSeenTransactionTimestamp;
    }
}
