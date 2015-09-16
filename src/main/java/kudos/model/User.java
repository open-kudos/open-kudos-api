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

    public User(String password, String email) {
        this.password = password;
        this.email = email;
    }

    private User() {

    }

    public User getUpdatedUser(MyProfileForm myProfileForm) throws MessagingException, IOException, TemplateException {
        String newEmail = myProfileForm.getEmail();
        String newPassword = myProfileForm.getNewPassword();
        String newFirstName = myProfileForm.getFirstName();
        String newLastName = myProfileForm.getLastName();

        User u = new User();

        u.email = !Strings.isNullOrEmpty(newEmail) ? newEmail : this.email;

        if (!Strings.isNullOrEmpty(newPassword) && !new StrongPasswordEncryptor().checkPassword(newPassword, this.password)) {
            u.password = new StrongPasswordEncryptor().encryptPassword(newPassword);
        } else {
            u.password = this.password;
        }

        if (!Strings.isNullOrEmpty(newFirstName) && !newFirstName.equals(this.firstName)) {
            u.firstName = newFirstName;
        }

        if (!Strings.isNullOrEmpty(newLastName) && !newLastName.equals(this.lastName)) {
            u.lastName = newLastName;
        }

        u.birthday = myProfileForm.getBirthday();
        u.phone = myProfileForm.getPhone();
        u.startedToWorkDate = myProfileForm.getStartedToWorkDate();
        u.position = myProfileForm.getPosition();
        u.department = myProfileForm.getDepartment();
        u.location = myProfileForm.getLocation();
        u.team = myProfileForm.getTeam();
        u.showBirthday = myProfileForm.getShowBirthday();
        u.isCompleted = true;

        return u;

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
}
