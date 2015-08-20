package kudos.web.beans.form;

import com.google.common.base.Strings;
import kudos.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by chc on 15.8.4.
 */
public class MyProfileForm {

    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String phone;
    private String startedToWorkDate;
    private String position;
    private String department;
    private String location;
    private String team;
    private boolean isCompleted = false;

    public boolean getShowBirthday() {
        return showBirthday;
    }

    public void setShowBirthday(boolean showBirthday) {
        this.showBirthday = showBirthday;
    }

    private boolean showBirthday = false;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartedToWorkDate() {
        return startedToWorkDate;
    }

    public void setStartedToWorkDate(String startedToWorkDate) {
        this.startedToWorkDate = startedToWorkDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isShowBirthday() {
        return showBirthday;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }


    public static class MyProfileValidator implements Validator{

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        private static final String PHONE_PATTERN = "([\\\\+(]?(\\\\d){2,}[)]?[- \\\\.]?(\\\\d)"+
                "{2,}[- \\\\.]?(\\\\d){2,}[- \\\\.]?(\\\\d){2,}[- \\\\.]?(\\\\d){2,})|([\\\\+(]?"+
                "(\\\\d){2,}[)]?[- \\\\.]?(\\\\d){2,}[- \\\\.]?(\\\\d){2,}[- \\\\.]?(\\\\d){2,})|"+
                "([\\\\+(]?(\\\\d){2,}[)]?[- \\\\.]?(\\\\d){2,}[- \\\\.]?(\\\\d){2,})";

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {

            MyProfileForm form = (MyProfileForm)target;

            String birthdayDate = form.getBirthday();
            if(Strings.isNullOrEmpty(birthdayDate)){
                errors.rejectValue("birthday","birthday.date.not.specified");
            }
            else if(!isEnteredDateValid(birthdayDate)){
                errors.rejectValue("birthday","birthday.date.incorrect");
            }

            String email = form.getEmail();
            if(!Strings.isNullOrEmpty(email) && !email.matches(EMAIL_PATTERN)){
                errors.rejectValue("email","incorrect.email");
            }

            String phone = form.getPhone();
            if(!Strings.isNullOrEmpty(phone)  && !phone.matches(PHONE_PATTERN)){
                errors.rejectValue("phone","incorrect.phone");
            }

            String startedToWorkDate = form.getStartedToWorkDate();
            if(Strings.isNullOrEmpty(startedToWorkDate)){
                errors.rejectValue("startedToWorkDate","startedToWorkDate.not.specified");
            } else if(!isEnteredDateValid(startedToWorkDate)){
                errors.rejectValue("startedToWorkDate","startedToWorkDate.incorrect");
            }


            String oldPassword = form.getOldPassword();
            if(!Strings.isNullOrEmpty(oldPassword)){

                String newPassword = form.getNewPassword();
                String newPasswordConfirm = form.getNewPasswordConfirm();
                if(Strings.isNullOrEmpty(newPassword)){
                    errors.rejectValue("newPassword","newPassword.not.specified");
                } else if(Strings.isNullOrEmpty(newPasswordConfirm)){
                    errors.rejectValue("newPasswordConfirm","newPasswordConfirm.not.specified");
                } else if(!newPassword.equals(newPasswordConfirm)){
                    errors.rejectValue("newPasswordConfirm","no.new.password.match");
                }

            }

        }

        public boolean isEnteredDateValid(String text) {
            if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
                return false;
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(text);
                return true;
            } catch (ParseException ex) {
                return false;
            }
        }

    }

}
