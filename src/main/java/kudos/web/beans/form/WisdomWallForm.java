package kudos.web.beans.form;


import com.google.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class WisdomWallForm {
    String authorName;
    String idea;

    public String getAuthorName() {
        return authorName;
    }

    public String getIdea() {
        return idea;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public static class WisdomWallFormValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {
            WisdomWallForm form = (WisdomWallForm) target;

            String authorName = form.getAuthorName();

            if (Strings.isNullOrEmpty(authorName)) {
                errors.rejectValue("authorName", "author_name_not_specified");
            }

            if (Strings.isNullOrEmpty(form.getIdea())) {
                errors.rejectValue("idea", "idea_not_specified");
            }
        }
    }
}
