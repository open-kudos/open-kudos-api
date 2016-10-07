package kudos.web.beans.response;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.FieldError;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ErrorResponse extends Response {

    private List<InputFieldError> fieldErrors;

    private InputFieldError fieldError;

    public ErrorResponse(List<InputFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public ErrorResponse(InputFieldError fieldError) {
        this.fieldError = fieldError;
    }

    public List<InputFieldError> getFieldErrors() {
        return fieldErrors;
    }

    public InputFieldError getFieldError() {
        return fieldError;
    }

    public static Response create(List<FieldError> fieldErrors, ReloadableResourceBundleMessageSource messageSource) {

        return new ErrorResponse(Lists.transform(fieldErrors, new Function<FieldError, InputFieldError>() {
            @Nullable
            @Override
            public InputFieldError apply(@Nullable FieldError input) {
                return new InputFieldError(input.getField(), input.getCode(),
                        messageSource.getMessage(input.getCode(), null, null));
            }
        }));
    }

    public static Response create(InputFieldError fieldError) {
        return new ErrorResponse(fieldError);
    }
}
