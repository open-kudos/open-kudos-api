package kudos.model;

import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by chc on 15.7.23.
 */
public class UserForm {

    private String password;
    private String confirmPassword;
    private String email;
    private String name;
    private String surname;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public User toUser() {
        return new User(password, email, name, surname);
    }

    public static class FormValidator implements Validator {

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        @Override
        public boolean supports(Class clazz) {
            return UserForm.class.equals(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            UserForm form = (UserForm) target;
            if (!form.getPassword().equals(form.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "no.match.password");
            }

            if (Strings.isNullOrEmpty(form.getEmail())) {
                errors.rejectValue("email", "required.email");
            }

            if(!form.getEmail().matches(EMAIL_PATTERN)){
                errors.rejectValue("email","incorrect.email");
            }

        }
    }
}