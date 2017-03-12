package kudos.web.beans.request.validator;

import kudos.web.beans.request.AddIdeaForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddIdeaFormValidator extends BaseValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return AddIdeaForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddIdeaForm form = (AddIdeaForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "author", "author_cannot_be_empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phrase", "phrase_cannot_be_empty");
    }
}

