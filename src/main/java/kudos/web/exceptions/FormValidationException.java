package kudos.web.exceptions;

import org.springframework.validation.Errors;

/**
 * Created by chc on 15.8.10.
 */
public class FormValidationException extends Exception {

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public Errors errors;

    public FormValidationException(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

}
