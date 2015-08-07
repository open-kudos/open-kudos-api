package kudos.model.object;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chc on 15.7.23.
 */
@Document
public class User {
    @Id
    @Indexed(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private String birthday;
    private String phone;

    private String startedToWorkDate;
    private String position;

    private boolean isCompleted = false;
    private boolean showBirthday = false;
    private boolean isConfirmed = false;

    private boolean isRegistered = true;

    private String department;
    private String location;
    private String team;

    public User(String password, String email,String firstName, String lastName){
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void updateUserWithAdditionalInformation(String password, String email, String firstName, String lastName, String birthday, String phone, String startedToWorkDate, String position, String department, String location,
                                                    String team, boolean isCompleted, boolean showBirthday){

        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.startedToWorkDate = startedToWorkDate;
        this.position = position;
        this.department = department;
        this.location = location;
        this.team = team;
        this.isCompleted = isCompleted;
        this.showBirthday = showBirthday;

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

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail(){
        return this.email;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void markUserAsConfirmed(){
        this.isConfirmed = true;
    }

}
