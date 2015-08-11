package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by chc on 15.8.7.
 */
public class ChallengeTransferForm {

    private String receiverEmail;
    private String judgeEmail;
    private String challengeName;
    private String estimatedDate;
    private String amount;

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getJudgeEmail() {
        return judgeEmail;
    }

    public void setJudgeEmail(String judge) {
        this.judgeEmail = judge;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static class ChallengeTransferFormValidator implements Validator {

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {
            ChallengeTransferForm challengeTransferForm = (ChallengeTransferForm)target;

            String receiverEmail = challengeTransferForm.getReceiverEmail();
            String judgeEmail = challengeTransferForm.getJudgeEmail();
            String amountInString = challengeTransferForm.getAmount();
            String challengeName = challengeTransferForm.getChallengeName();
            String estimatedDate = challengeTransferForm.getEstimatedDate();

            if(Strings.isNullOrEmpty(receiverEmail)){
                errors.rejectValue("receiverEmail","receiver.email.not.specified");
            } else if(!receiverEmail.matches(EMAIL_PATTERN)){
                errors.rejectValue("receiverEmail","receiver.email.incorrect");
            }

            if(Strings.isNullOrEmpty(judgeEmail)){
                errors.rejectValue("judgeEmail","judge.email.not.specified");
            } else if(!judgeEmail.matches(EMAIL_PATTERN)){
                errors.rejectValue("judgeEmail","judge.email.incorrect");
            }

            if (!Strings.isNullOrEmpty(amountInString)) {
                try {
                    int amount = Integer.parseInt(amountInString);
                    if (amount <= 0) {
                        errors.rejectValue("amount", "amount.negative");
                    }
                } catch (NumberFormatException e) {
                    errors.rejectValue("amount", "amount.not.digit");
                }
            }

            if(Strings.isNullOrEmpty(challengeName)){
                errors.rejectValue("challengeName","challenge.name.is.empty");
            }

            if(!isEnteredDateValid(estimatedDate)){
                errors.rejectValue("estimatedDate","estimatedData.is.empty");
            } else if(Strings.isNullOrEmpty(estimatedDate)){
                errors.rejectValue("estimatedDate","estimatedDate.is.empty");
            }

        }

        public boolean isEnteredDateValid(String text) {
            if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
                return false;
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(text);
                return true;
            } catch (ParseException ex) {
                return false;
            }
        }

    }

}
