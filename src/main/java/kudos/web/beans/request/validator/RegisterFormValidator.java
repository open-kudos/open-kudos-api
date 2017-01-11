package kudos.web.beans.request.validator;

import kudos.web.beans.request.RegisterForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegisterFormValidator extends BaseValidator implements Validator {

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

        if(isEmailWrongPattern(form.getEmail())){
            errors.rejectValue("email", "email_incorrect_pattern");
        }

        if (isFirstNameTooLong(form.getFirstName()))
            errors.rejectValue("firstName", "first_name_too_long");

        if (isLastNameTooLong(form.getLastName()))
            errors.rejectValue("lastName", "last_name_too_long");
    }
}

