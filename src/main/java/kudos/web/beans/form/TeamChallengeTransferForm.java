package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.joda.time.format.DateTimeFormat;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;

@ApiObject
public class TeamChallengeTransferForm {
    @ApiObjectField
    private String name;
    @ApiObjectField
    private Map<String, Boolean> firstTeam;
    @ApiObjectField
    private Map<String, Boolean> secondTeam;
    @ApiObjectField
    private String description;
    @ApiObjectField
    private String finishDate;
    @ApiObjectField
    private String amount;

    public String getName() {
        return name;
    }

    public Map<String, Boolean> getFirstTeam() {
        return firstTeam;
    }

    public Map<String, Boolean> getSecondTeam() {
        return secondTeam;
    }

    public String getDescription() {
        return description;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstTeam(Map<String, Boolean> firstTeam) {
        this.firstTeam = firstTeam;
    }

    public void setSecondTeam(Map<String, Boolean> secondTeam) {
        this.secondTeam = secondTeam;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static class TeamChallengeTransferFormValidator implements Validator {

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {
            TeamChallengeTransferForm teamChallengeTransferForm = (TeamChallengeTransferForm) target;

            for (Map.Entry<String, Boolean> participant : teamChallengeTransferForm.getFirstTeam().entrySet()) {
                String participantEmail = participant.getKey();
                if (Strings.isNullOrEmpty(participantEmail)) {
                    errors.rejectValue("participant", "participant_email_not_specified");
                } else if (!participantEmail.matches(EMAIL_PATTERN)) {
                    errors.rejectValue("participant", "participant_email_incorrect");
                }
            }

            for (Map.Entry<String, Boolean> participant : teamChallengeTransferForm.getSecondTeam().entrySet()) {
                String participantEmail = participant.getKey();
                if (Strings.isNullOrEmpty(participantEmail)) {
                    errors.rejectValue("participant", "participant_email_not_specified");
                } else if (!participantEmail.matches(EMAIL_PATTERN)) {
                    errors.rejectValue("participant", "participant_email_incorrect");
                }
            }

            String amountInString = teamChallengeTransferForm.getAmount();
            String challengeName = teamChallengeTransferForm.getName();
            String estimatedDate = teamChallengeTransferForm.getFinishDate();

            if (Strings.isNullOrEmpty(amountInString)) {
                errors.rejectValue("amount", "amount_not_specified");
            }

            try {
                int amount = Integer.parseInt(amountInString);
                if (amount <= 0) {
                    errors.rejectValue("amount", "amount_negative_or_zero");
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("amount", "amount_not_digit");
            }

            if (Strings.isNullOrEmpty(challengeName)) {
                errors.rejectValue("name", "name_not_specified");
            }

            if (!Strings.isNullOrEmpty(estimatedDate)) {
                if (!isEnteredDateValid(estimatedDate)) {
                    errors.rejectValue("finishDate", "finishDate_incorrect");
                }
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
