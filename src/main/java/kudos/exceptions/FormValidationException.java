package kudos.exceptions;

import org.springframework.validation.Errors;

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
