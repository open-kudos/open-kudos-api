package kudos.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
    @Id
    @Indexed(unique = true)
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String birthday;
    private String startedToWorkDate;
    private String emailHash;
    private UserStatus status;
    private boolean isSubscribing;
    private int totalKudos;
    private int weeklyKudos;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isSubscribing() {
        return isSubscribing;
    }

    public void setSubscribing(boolean subscribing) {
        isSubscribing = subscribing;
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
