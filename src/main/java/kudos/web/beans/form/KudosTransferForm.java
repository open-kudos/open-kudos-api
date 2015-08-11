package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by chc on 15.8.5.
 */
public class KudosTransferForm {

    private String receiverEmail;
    private String message;
    private String amount;

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static class KudosFormValidator implements Validator {

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {
            KudosTransferForm form = (KudosTransferForm) target;

            String receiverEmail = form.getReceiverEmail();

            if (Strings.isNullOrEmpty(receiverEmail)) {
                errors.rejectValue("receiverEmail", "receiver.email.not.specified");
            } else if (!receiverEmail.matches(EMAIL_PATTERN)) {
                errors.rejectValue("receiverEmail", "receiver.email.incorrect");
            }

            if (!Strings.isNullOrEmpty(form.getAmount())) {
                try {
                    int amount = Integer.parseInt(form.getAmount());
                    if (amount <= 0) {
                        errors.rejectValue("amount", "amount.negative");
                    }
                } catch (NumberFormatException e) {
                    errors.rejectValue("amount", "amount.not.digit");
                }
            }
        }
    }
}

