package kudos.web.beans.response;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.validation.FieldError;

import javax.annotation.Nullable;
import java.util.List;

public class ErrorResponse extends Response {

    private final List<InputFieldError> fieldErrors;

    public ErrorResponse(List<InputFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<InputFieldError> getFieldErrors() {
        return fieldErrors;
    }

    public static Response create(List<FieldError> fieldErrors) {

        return new ErrorResponse(Lists.transform(fieldErrors, new Function<FieldError, InputFieldError>() {
            @Nullable
            @Override
            public InputFieldError apply(@Nullable FieldError input) {
                return new InputFieldError(input.getField(), input.getCode());
            }
        }));
    }
}
