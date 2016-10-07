package kudos.web.beans.request.validator;

import kudos.web.beans.request.RegisterForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegisterFormValidator implements Validator {

    private String domain = "swedbank";

    private String EmailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
            "@" + domain + "+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Value("${kudos.maxNameLength}")
    private int maxNameLength;

    @Override
    public boolean supports(Class clazz) {
        return RegisterForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterForm form = (RegisterForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "first_name_is_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email_is_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "last_name_is_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password_not_specified");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "confirm_password_not_specified");

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "no_match_password");
        }

        if(!form.getEmail().matches(EmailPattern)){
            errors.rejectValue("email", "email_incorrect_pattern");
        }

        if (form.getFirstName().length() > maxNameLength)
            errors.rejectValue("firstName", "first_name_too_long");

        if (form.getLastName().length() > maxNameLength)
            errors.rejectValue("lastName", "last_name_too_long");
    }
}

