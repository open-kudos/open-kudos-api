package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by chc on 15.8.11.
 */
public class LoginForm {

    private String email;
    private String password;

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

    public static class LoginFormValidator implements Validator {

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        @Override
        public boolean supports(Class clazz) {
            return LoginForm.class.equals(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {

            final LoginForm form = (LoginForm) target;
            final String email = form.getEmail();
            final String password = form.getPassword();

            if (Strings.isNullOrEmpty(email)) {
                errors.rejectValue("email", "email.not.specified");
            } else if (!email.matches(EMAIL_PATTERN)) {
                errors.rejectValue("email", "email.incorrect");
            }

            if (Strings.isNullOrEmpty(password)) {
                errors.rejectValue("password", "password.not.specified");
            }
        }
    }

}