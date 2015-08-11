package kudos.web.handlers;

import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.ErrorResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by chc on 15.8.10.
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    private final Logger LOG = Logger.getLogger(ErrorHandler.class.getName());

    @ExceptionHandler(FormValidationException.class)
    public ResponseEntity<Response> handleFromValidationException(HttpServletRequest request, FormValidationException exception)   {
        LOG.info("error count is: " + exception.getErrors().getErrorCount());
        Errors errors = exception.getErrors();
        return new ResponseEntity<>(ErrorResponse.create(errors.getFieldErrors()), HttpStatus.BAD_REQUEST);
    }

}
