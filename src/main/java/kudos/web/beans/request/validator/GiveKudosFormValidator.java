package kudos.web.beans.request.validator;

import kudos.web.beans.request.GiveKudosForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GiveKudosFormValidator extends BaseValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return GiveKudosForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiveKudosForm form = (GiveKudosForm) target;

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

