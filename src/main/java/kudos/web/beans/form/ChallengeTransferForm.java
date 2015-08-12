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

    private String participant;
    private String referee;
    private String name;
    private String finishDate;
    private String amount;

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String judge) {
        this.referee = judge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
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

            String receiverEmail = challengeTransferForm.getParticipant();
            String judgeEmail = challengeTransferForm.getReferee();
            String amountInString = challengeTransferForm.getAmount();
            String challengeName = challengeTransferForm.getName();
            String estimatedDate = challengeTransferForm.getFinishDate();

            if(Strings.isNullOrEmpty(receiverEmail)){
                errors.rejectValue("participant","receiver.email.not.specified");
            } else if(!receiverEmail.matches(EMAIL_PATTERN)){
                errors.rejectValue("participant","receiver.email.incorrect");
            }

            if(Strings.isNullOrEmpty(judgeEmail)){
                errors.rejectValue("referee","referee.not.specified");
            } else if(!judgeEmail.matches(EMAIL_PATTERN)){
                errors.rejectValue("referee","referee.incorrect");
            }

            if (!Strings.isNullOrEmpty(amountInString)) {
                try {
                    int amount = Integer.parseInt(amountInString);
                    if (amount <= 0) {
                        errors.rejectValue("amount", "amount.negative.or.zero");
                    }
                } catch (NumberFormatException e) {
                    errors.rejectValue("amount", "amount.not.digit");
                }
            }

            if(Strings.isNullOrEmpty(challengeName)){
                errors.rejectValue("name","create.name.is.empty");
            }

            if(!isEnteredDateValid(estimatedDate)){
                errors.rejectValue("finishDate","finishDate.is.empty");
            } else if(Strings.isNullOrEmpty(estimatedDate)){
                errors.rejectValue("finishDate","finishDate.is.empty");
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
