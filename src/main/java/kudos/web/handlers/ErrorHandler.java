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
    public ResponseEntity<String> handleInvalidKudosAmountException(HttpServletRequest request,
                                                                    InvalidKudosAmountException e){
        return new ResponseEntity<>(e.getError(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(HttpServletRequest request, BusinessException e){
        return new ResponseEntity<>(e.getError(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException(HttpServletRequest request, MessagingException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<String> handleMessagingException(HttpServletRequest request, MongoException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Response> handleOccupiedEmailException(HttpServletRequest request, UserException e){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return new ResponseEntity<>(ErrorResponse.create(new InputFieldError(null, e.getErrorCause(),
                messageSource.getMessage(e.getErrorCause(), null, null))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> handleAuthenticationCredentialsNotFoundException(HttpServletRequest request,
                                                                 AuthenticationCredentialsNotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<String> handleIntegerParseException(HttpServletRequest request, ParseException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChallengeException.class)
    public ResponseEntity<String> handleChallengeException(HttpServletRequest request, ChallengeException e){
        return new ResponseEntity<>(e.getErrorCause(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RelationException.class)
    public ResponseEntity<String> handleRelationExceptionException(HttpServletRequest request, RelationException e){
        return new ResponseEntity<>(e.getErrorCause(),HttpStatus.BAD_REQUEST);
    }

}
