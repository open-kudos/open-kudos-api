package kudos.web.beans.request.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseValidator {

    private String domain = "swedbank";

    private String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"+
            "@" + domain + "+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Value("${kudos.maxNameLength}")
    private int maxNameLength;

    public boolean isFirstNameTooLong(String firstName) {
        return firstName.length() > maxNameLength;
    }

    public boolean isLastNameTooLong(String lastName) {
        return lastName.length() > maxNameLength;
    }

    public boolean isEmailWrongPattern(String email) {
        return !email.matches(emailPattern);
    }

}

