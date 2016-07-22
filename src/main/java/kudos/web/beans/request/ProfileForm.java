package kudos.web.beans.request;

public class ProfileForm {

    private String firstName;
    private String lastName;
    private String birthday;
    private String startedToWork;

    public ProfileForm() {}

    public ProfileForm(String firstName, String lastName, String birthday, String startedToWork) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.startedToWork =startedToWork;
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

    public String getStartedToWork() {
        return startedToWork;
    }

    public void setStartedToWork(String startedToWork) {
        this.startedToWork = startedToWork;
    }
}
