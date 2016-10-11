package kudos.web.beans.request.validator;

import kudos.web.beans.request.GiveChallengeForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GiveChallengeFormValidator extends BaseValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return GiveChallengeForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiveChallengeForm form = (GiveChallengeForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "challenge_name_is_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "receiverEmail", "receiver_email_is_required");
        ValidationUtils.rejectIfEmpty(errors, "amount", "invalid_kudos_amount");

        if (form.getAmount() < 1) {
            errors.rejectValue("amount", "invalid_kudos_amount");
        }

        if(isEmailWrongPattern(form.getReceiverEmail())){
            errors.rejectValue("receiverEmail", "email_incorrect_pattern");
        }
    }
}

