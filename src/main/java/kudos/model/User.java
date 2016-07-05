package kudos.model;


import com.google.common.base.Strings;
import freemarker.template.TemplateException;
import kudos.web.beans.form.MyProfileForm;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jsondoc.core.annotation.ApiObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by chc on 15.7.23.
 */
@ApiObject
@Document
public class User {
    @Id
    @Indexed(unique = true)
    protected String id;
    protected String email;
    protected String password;
    protected String firstName;
    protected String lastName;

    protected String birthday;
    protected String phone;

    protected String startedToWorkDate;
    protected String position;

    protected String emailHash;

    protected boolean isCompleted = false;
    protected boolean showBirthday = false;
    protected boolean isConfirmed = false;

    protected String department;
    protected String location;
    protected String team;

    protected String lastSeenTransactionTimestamp;

    public User(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public User() {}

    public User getUpdatedUser(MyProfileForm myProfileForm) throws MessagingException, IOException, TemplateException {
        String newEmail = myProfileForm.getEmail();
        String newPassword = myProfileForm.getNewPassword();
        String newFirstName = myProfileForm.getFirstName();
        String newLastName = myProfileForm.getLastName();

        this.email = !Strings.isNullOrEmpty(newEmail) ? newEmail : this.email;

        if (!Strings.isNullOrEmpty(newPassword) && !new StrongPasswordEncryptor().checkPassword(newPassword, this.password)) {
            this.password = new StrongPasswordEncryptor().encryptPassword(newPassword);
        }

        if (!Strings.isNullOrEmpty(newFirstName) && !newFirstName.equals(this.firstName)) {
            this.firstName = newFirstName;
        }

        if (!Strings.isNullOrEmpty(newLastName) && !newLastName.equals(this.lastName)) {
            this.lastName = newLastName;
        }

        this.birthday = myProfileForm.getBirthday();
        this.phone = myProfileForm.getPhone();
        this.startedToWorkDate = myProfileForm.getStartedToWorkDate();
        this.position = myProfileForm.getPosition();
        this.department = myProfileForm.getDepartment();
        this.location = myProfileForm.getLocation();
        this.team = myProfileForm.getTeam();
        this.showBirthday = myProfileForm.getShowBirthday();

        this.isCompleted = isUserCompleted();

        return this;

    }

    public String getId() {
        return id;
    }

    private String getRandomHash() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    public String getDepartment() {
        return department;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getStartedToWorkDate() {
        return startedToWorkDate;
    }

    public String getPosition() {
        return position;
    }

    public String getLocation() {
        return location;
    }

    public String getTeam() {
        return team;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isShowBirthday() {
        return showBirthday;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void markUserAsConfirmed() {
        this.isConfirmed = true;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    public String getLastSeenTransactionTimestamp() {
        return lastSeenTransactionTimestamp;
    }

    public void setLastSeenTransactionTimestamp(String lastSeenTransactionTimestamp) {
        this.lastSeenTransactionTimestamp = lastSeenTransactionTimestamp;
    }

    private boolean isUserCompleted() {
        return !Strings.isNullOrEmpty(this.getStartedToWorkDate()) && !Strings.isNullOrEmpty(this.getBirthday());
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
