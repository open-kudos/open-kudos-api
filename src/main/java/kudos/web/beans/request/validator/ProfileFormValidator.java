package kudos.web.beans.request.validator;

import kudos.web.beans.request.ProfileForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProfileFormValidator extends BaseValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return ProfileForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileForm form = (ProfileForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "first_name_is_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "last_name_is_required");

        if (isFirstNameTooLong(form.getFirstName()))
            errors.rejectValue("firstName", "first_name_too_long");

        if (isLastNameTooLong(form.getLastName()))
            errors.rejectValue("lastName", "last_name_too_long");
    }
}

