package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by chc on 15.8.11.
 */
@ApiObject
public class LoginForm {

    @ApiObjectField
    private String email;
    @ApiObjectField
    private String domainSuffix;
    @ApiObjectField
    private String password;

    public LoginForm(){}

    public LoginForm(String email, String domainSuffix, String password) {
        this.email = email;
        this.domainSuffix = domainSuffix;
        this.password = password;
    }

    public void setDomainSuffix(String domainSuffix) {
        this.domainSuffix = domainSuffix;
    }

    public String getDomainSuffix() {
        return domainSuffix;
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

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*";

        @Override
        public boolean supports(Class clazz) {
            return LoginForm.class.equals(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {

            final LoginForm form = (LoginForm) target;
            final String email = form.getEmail();
            final String domainSuffix = form.getDomainSuffix();
            final String password = form.getPassword();

            if (Strings.isNullOrEmpty(email)) {
                errors.rejectValue("email", "email_not_specified");
            } else if (!email.matches(EMAIL_PATTERN)) {
                errors.rejectValue("email", "email_incorrect");
            }

            if (Strings.isNullOrEmpty(domainSuffix)) {
                errors.rejectValue("domainSuffix", "domain_suffix_not_specified");
            }

            if (Strings.isNullOrEmpty(password)) {
                errors.rejectValue("password", "password_not_specified");
            }
        }
    }

}
