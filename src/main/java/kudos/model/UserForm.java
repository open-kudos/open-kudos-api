package kudos.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * Created by chc on 15.7.23.
 */
public class UserForm {

    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
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

            if (StringUtils.isEmpty(form.getEmail())) {
                errors.rejectValue("email", "required.email");
            }

            if(!form.getEmail().matches(EMAIL_PATTERN)){
                errors.rejectValue("email","incorrect.email");
            }

        }
    }
}
