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

    private String participantEmail;
    private String refereeEmail;
    private String challengeName;
    private String dueDate;
    private String amount;

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public String getRefereeEmail() {
        return refereeEmail;
    }

    public void setRefereeEmail(String judge) {
        this.refereeEmail = judge;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

            String receiverEmail = challengeTransferForm.getParticipantEmail();
            String judgeEmail = challengeTransferForm.getRefereeEmail();
            String amountInString = challengeTransferForm.getAmount();
            String challengeName = challengeTransferForm.getChallengeName();
            String estimatedDate = challengeTransferForm.getDueDate();

            if(Strings.isNullOrEmpty(receiverEmail)){
                errors.rejectValue("participantEmail","receiver.email.not.specified");
            } else if(!receiverEmail.matches(EMAIL_PATTERN)){
                errors.rejectValue("participantEmail","receiver.email.incorrect");
            }

            if(Strings.isNullOrEmpty(judgeEmail)){
                errors.rejectValue("refereeEmail","judge.email.not.specified");
            } else if(!judgeEmail.matches(EMAIL_PATTERN)){
                errors.rejectValue("refereeEmail","judge.email.incorrect");
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
                errors.rejectValue("dueDate","estimatedData.is.empty");
            } else if(Strings.isNullOrEmpty(estimatedDate)){
                errors.rejectValue("dueDate","dueDate.is.empty");
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
