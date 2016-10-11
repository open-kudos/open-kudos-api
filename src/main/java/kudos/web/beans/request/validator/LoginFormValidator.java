package kudos.web.beans.request.validator;

import kudos.web.beans.request.LoginForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LoginFormValidator extends BaseValidator implements Validator  {

    @Override
    public boolean supports(Class clazz) {
        return LoginForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginForm form = (LoginForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email_is_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password_not_specified");

        if(isEmailWrongPattern(form.getEmail())) {
            errors.rejectValue("email", "email_incorrect_pattern");
        }
    }
}

