package kudos.web.handlers;

import com.mongodb.MongoException;
import kudos.exceptions.*;
import kudos.exceptions.FormValidationException;
import kudos.web.beans.response.InputFieldError;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.ErrorResponse;
import kudos.exceptions.UserException;
import org.apache.log4j.Logger;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    private final Logger LOG = Logger.getLogger(ErrorHandler.class.getName());



    @ExceptionHandler(FormValidationException.class)
    public ResponseEntity<Response> handleFromValidationException(HttpServletRequest request,
                                                                  FormValidationException exception)   {
        LOG.info("error count is: " + exception.getErrors().getErrorCount());
        Errors errors = exception.getErrors();
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(errors.getFieldErrors(), messageSource), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidKudosAmountException.class)
    public ResponseEntity<Response> handleInvalidKudosAmountException(HttpServletRequest request, InvalidKudosAmountException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, e.getErrorCause(),
                messageSource.getMessage(e.getErrorCause(), null, null))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Response> handleMessagingException(HttpServletRequest request, MessagingException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, "undeclared_error",
                messageSource.getMessage("undeclared_error", null, null))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<Response> handleMessagingException(HttpServletRequest request, MongoException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, "undeclared_error",
                messageSource.getMessage("undeclared_error", null, null))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Response> handleOccupiedEmailException(HttpServletRequest request, UserException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, e.getErrorCause(),
                messageSource.getMessage(e.getErrorCause(), null, null))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Response> handleAuthenticationCredentialsNotFoundException(HttpServletRequest request,
                                                                 AuthenticationCredentialsNotFoundException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, "wrong_credentials",
                messageSource.getMessage("wrong_credentials", null, null))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Response> handleIntegerParseException(HttpServletRequest request, ParseException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, "undeclared_error",
                messageSource.getMessage("undeclared_error", null, null))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChallengeException.class)
    public ResponseEntity<Response> handleChallengeException(HttpServletRequest request, ChallengeException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, e.getErrorCause(),
                messageSource.getMessage(e.getErrorCause(), null, null))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RelationException.class)
    public ResponseEntity<Response> handleRelationExceptionException(HttpServletRequest request, RelationException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, e.getErrorCause(),
                messageSource.getMessage(e.getErrorCause(), null, null))), HttpStatus.BAD_REQUEST);
    }

}
