package kudos.web.beans.form;

import com.google.common.base.Strings;
import kudos.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by chc on 15.7.23.
 */
public class UserForm {

    private String password;
    private String confirmPassword;
    private String email;

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

    public User toUser(){
        return new User(password,email);
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

            if (form.getPassword() != null && form.getConfirmPassword() != null &&
                    !form.getPassword().equals(form.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "no_match_password");
            }

            if (Strings.isNullOrEmpty(form.getEmail())) {
                errors.rejectValue("email", "email_not_specified");
            } else if(!form.getEmail().matches(EMAIL_PATTERN)){
                errors.rejectValue("email", "email_incorrect");
            }

            if(Strings.isNullOrEmpty(form.getPassword())){
                errors.rejectValue("password", "password_not_specified");
            }

            if(Strings.isNullOrEmpty(form.getConfirmPassword())){
                errors.rejectValue("confirmPassword", "confirm_password_not_specified");
            }

        }
    }
}
