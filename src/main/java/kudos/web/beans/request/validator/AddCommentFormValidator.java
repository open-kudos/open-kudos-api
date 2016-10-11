package kudos.web.beans.request.validator;

import kudos.web.beans.request.AddCommentForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddCommentFormValidator extends BaseValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return AddCommentForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddCommentForm form = (AddCommentForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "comment", "comment_cannot_be_empty");
    }
}

