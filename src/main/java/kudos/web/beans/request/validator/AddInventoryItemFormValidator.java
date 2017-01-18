package kudos.web.beans.request.validator;

import kudos.web.beans.request.AddInventoryItemForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddInventoryItemFormValidator extends BaseValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return AddInventoryItemForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddInventoryItemForm form = (AddInventoryItemForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "item_name_is_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "item_description_is_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pictureUrl", "item_picture_is_required");
        ValidationUtils.rejectIfEmpty(errors, "amount", "invalid_item_amount");
        ValidationUtils.rejectIfEmpty(errors, "price", "invalid_item_price");

        if (form.getAmount() < 1) {
            errors.rejectValue("amount", "invalid_item_amount");
        }

        if (form.getPrice() < 1) {
            errors.rejectValue("price", "invalid_item_price");
        }

//        if(isUrlWrongPattern(form.getPictureUrl())){
//            errors.rejectValue("pictureUrl", "picture_url_incorrect_pattern");
//        }
    }
}

