package kudos.web.beans.request;

import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



public class FormValidator implements Validator {

    private String EmailPattern;
    public FormValidator(String domain) {
        EmailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
                "@" + domain + "+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    }

    @Override
    public boolean supports(Class clazz) {
        return RegisterForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        RegisterForm form = (RegisterForm) target;

        if (form.getPassword() != null && form.getConfirmPassword() != null &&
                !form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "no_match_password");
        }

        if (Strings.isNullOrEmpty(form.getEmail())) {
            errors.rejectValue("email", "email_not_specified");
        } else if(!form.getEmail().matches(EmailPattern)){
            errors.rejectValue("email", "email_incorrect");
        }

        if (Strings.isNullOrEmpty(form.getFirstName())) {
            errors.rejectValue("firstName", "first_name_is_required");
        }

        if (Strings.isNullOrEmpty(form.getLastName())) {
            errors.rejectValue("lastName", "last_name_is_required");
        }

        if(Strings.isNullOrEmpty(form.getPassword())){
            errors.rejectValue("password", "password_not_specified");
        }

        if(Strings.isNullOrEmpty(form.getConfirmPassword())){
            errors.rejectValue("confirmPassword", "confirm_password_not_specified");
        }

    }
}

