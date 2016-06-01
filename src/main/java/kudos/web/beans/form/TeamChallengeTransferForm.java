package kudos.web.beans.form;

import com.google.common.base.Strings;
import org.joda.time.format.DateTimeFormat;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@ApiObject
public class TeamChallengeTransferForm {
    @ApiObjectField
    private String name;
    @ApiObjectField
    private List<String> firstTeam;
    @ApiObjectField
    private List<String> secondTeam;
    @ApiObjectField
    private String description;
    @ApiObjectField
    private String amount;

    public String getName() {
        return name;
    }

    public List<String> getFirstTeam() {
        return firstTeam;
    }

    public List<String> getSecondTeam() {
        return secondTeam;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstTeam(List<String> firstTeam) {
        this.firstTeam = firstTeam;
    }

    public void setSecondTeam(List<String> secondTeam) {
        this.secondTeam = secondTeam;
    }

    public void setDescription(String description) {
        this.description = description;
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

            for (String participant : teamChallengeTransferForm.getFirstTeam()) {
                if (Strings.isNullOrEmpty(participant)) {
                    errors.rejectValue("participant", "participant_email_not_specified");
                } else if (!participant.matches(EMAIL_PATTERN)) {
                    errors.rejectValue("participant", "participant_email_incorrect");
                }
            }

            for (String participant : teamChallengeTransferForm.getSecondTeam()) {
                if (Strings.isNullOrEmpty(participant)) {
                    errors.rejectValue("participant", "participant_email_not_specified");
                } else if (!participant.matches(EMAIL_PATTERN)) {
                    errors.rejectValue("participant", "participant_email_incorrect");
                }
            }

            String amountInString = teamChallengeTransferForm.getAmount();
            String challengeName = teamChallengeTransferForm.getName();

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

        }
    }
}
