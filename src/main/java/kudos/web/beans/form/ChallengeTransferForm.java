package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by chc on 15.8.7.
 */
@ApiObject
public class ChallengeTransferForm {

    @ApiObjectField
    private String participant;
    @ApiObjectField
    private String referee;
    @ApiObjectField
    private String name;
    @ApiObjectField
    private String finishDate;
    @ApiObjectField
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

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {
            ChallengeTransferForm challengeTransferForm = (ChallengeTransferForm) target;

            String receiverEmail = challengeTransferForm.getParticipant();
            String judgeEmail = challengeTransferForm.getReferee();
            String amountInString = challengeTransferForm.getAmount();
            String challengeName = challengeTransferForm.getName();
            String estimatedDate = challengeTransferForm.getFinishDate();

            if (Strings.isNullOrEmpty(receiverEmail)) {
                errors.rejectValue("participant", "receiver.email.not.specified");
            } else if (!receiverEmail.matches(EMAIL_PATTERN)) {
                errors.rejectValue("participant", "receiver.email.incorrect");
            }

            if (Strings.isNullOrEmpty(judgeEmail)) {
                errors.rejectValue("referee", "referee.email.not.specified");
            } else if (!judgeEmail.matches(EMAIL_PATTERN)) {
                errors.rejectValue("referee", "referee.email.incorrect");
            }

            if (Strings.isNullOrEmpty(amountInString)) {
                errors.rejectValue("amount", "amount.not.specified");
            }

            try {
                int amount = Integer.parseInt(amountInString);
                if (amount <= 0) {
                    errors.rejectValue("amount", "amount.negative.or.zero");
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("amount", "amount.not.digit");
            }


            if (Strings.isNullOrEmpty(challengeName)) {
                errors.rejectValue("name", "name.not.specified");
            }

            if (Strings.isNullOrEmpty(estimatedDate)) {
                errors.rejectValue("finishDate", "finishDate.not.specified");
            } else if (!isEnteredDateValid(estimatedDate)) {
                errors.rejectValue("finishDate", "finishDate.incorrect");
            }

        }

        public boolean isEnteredDateValid(String text) {
            try {
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").parseLocalDate(text);
                return true;
            } catch (IllegalArgumentException p) {
                return false;
            }

        }

    }

}
