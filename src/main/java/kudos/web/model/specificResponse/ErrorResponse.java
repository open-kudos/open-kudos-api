package kudos.web.model.specificResponse;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import kudos.web.model.mainResponse.Response;
import org.springframework.validation.FieldError;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by chc on 15.7.30.
 */
public class ErrorResponse extends Response {

    private final List<InputFieldError> fieldErrors;

    public ErrorResponse(List<InputFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<InputFieldError> getFieldsError() {
        return fieldErrors;
    }

    public static Response create(List<FieldError> fieldErrors) {
        //fieldErrors.stream().map()
        return new ErrorResponse(Lists.transform(fieldErrors, new Function<FieldError, InputFieldError>() {
            @Nullable
            @Override
            public InputFieldError apply(@Nullable FieldError input) {
                return new InputFieldError(input.getField(), input.getCode());
            }
        }));
    }
}
