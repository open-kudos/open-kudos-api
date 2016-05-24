package kudos.web.beans.form;


import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class WisdomWallForm {
    String authorEmail;
    String idea;

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getIdea() {
        return idea;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public static class WisdomWallFormValidator implements Validator {

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {
            WisdomWallForm form = (WisdomWallForm) target;

            String authorEmail = form.getAuthorEmail();

            if (Strings.isNullOrEmpty(authorEmail)) {
                errors.rejectValue("authorEmail", "author_email_not_specified");
            } else if (!authorEmail.matches(EMAIL_PATTERN)) {
                errors.rejectValue("authorEmail", "author_email_incorrect");
            }

            if (Strings.isNullOrEmpty(form.getIdea())) {
                errors.rejectValue("idea", "idea_not_specified");
            }
        }
    }
}
