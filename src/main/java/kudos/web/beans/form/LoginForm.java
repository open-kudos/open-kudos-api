package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@ApiObject
public class LoginForm {

    @ApiObjectField
    private String email;
    @ApiObjectField
    private String password;

    public LoginForm(){}

    public LoginForm(String email, String password) {
        this.email = email;
        this.password = password;
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

    public static class LoginFormValidator implements Validator {

        private String EmailPattern;
        public LoginFormValidator(String domain) {
            EmailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
                    "@" + domain + "+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        }

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
                errors.rejectValue("email", "email_not_specified");
            } else if (!email.matches(EmailPattern)) {
                errors.rejectValue("email", "email_incorrect");
            }

            if (Strings.isNullOrEmpty(password)) {
                errors.rejectValue("password", "password_not_specified");
            }
        }
    }

}
