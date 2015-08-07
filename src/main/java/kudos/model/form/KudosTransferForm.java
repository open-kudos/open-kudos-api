package kudos.model.form;

import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by chc on 15.8.5.
 */
public class KudosTransferForm {

    private String receiverEmail;
    private String message;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public static class KudosFormValidator implements Validator{

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        private static final String MINIMUM_KUDOS_TYPE = "MINIMUM";
        private static final String NORMAL_KUDOS_TYPE = "NORMAL";
        private static final String MAXIMUM_KUDOS_TYPE = "MAXIMUM";

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {
            KudosTransferForm form = (KudosTransferForm)target;

            String receiverEmail = form.getReceiverEmail();
            String type = form.getType().toUpperCase();

            if(Strings.isNullOrEmpty(receiverEmail)){
                errors.rejectValue("receiverEmail","receiver.email.not.specified");
            } else if(!receiverEmail.matches(EMAIL_PATTERN)){
                errors.rejectValue("receiverEmail","receiver.email.incorrect");
            }

            if(!type.matches(MINIMUM_KUDOS_TYPE) && !type.matches(NORMAL_KUDOS_TYPE) && !type.matches(MAXIMUM_KUDOS_TYPE) && !Strings.isNullOrEmpty(type)){
                errors.rejectValue("type","type.is.incorrect");
            } else if (Strings.isNullOrEmpty(type)) {
                errors.rejectValue("type","type.not.specified");
            }

        }
    }

}
