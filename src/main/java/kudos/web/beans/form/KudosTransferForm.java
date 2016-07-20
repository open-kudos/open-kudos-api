package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
                errors.rejectValue("receiverEmail", "receiver_email_not_specified");
            } else if (!receiverEmail.matches(EMAIL_PATTERN)) {
                errors.rejectValue("receiverEmail", "receiver_email_incorrect");
            }

            if (!Strings.isNullOrEmpty(form.getAmount())) {
                try {
                    int amount = Integer.parseInt(form.getAmount());
                    if (amount <= 0) {
                        errors.rejectValue("amount", "amount_negative_or_zero");
                    }
                } catch (NumberFormatException e) {
                    errors.rejectValue("amount", "amount_not_digit");
                }
            }
        }
    }
}


