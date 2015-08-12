package kudos.web.handlers;

import com.mongodb.MongoException;
import kudos.exceptions.*;
import kudos.web.beans.response.SingleErrorResponse;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.ErrorResponse;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
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

    @ExceptionHandler(InvalidKudosAmountException.class)
    public ResponseEntity<Response> handleInvalidKudosAmountException(HttpServletRequest request, InvalidKudosAmountException e){
        return new ResponseEntity<>(new SingleErrorResponse(e.getError()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response> handleBusinessException(HttpServletRequest request, InvalidKudosAmountException e){
        return new ResponseEntity<>(new SingleErrorResponse(e.getError()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Response> handleMessagingException(HttpServletRequest request, MessagingException e){
        return new ResponseEntity<>(new SingleErrorResponse(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<Response> handleMessagingException(HttpServletRequest request, MongoException e){
        return new ResponseEntity<>(new SingleErrorResponse(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Response> handleOccupiedEmailException(HttpServletRequest request, UserException e){
        return new ResponseEntity<>(new SingleErrorResponse(e.getErrorCause()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidChallengeStatusException.class)
    public ResponseEntity<Response> handleInvalidChallengeStatusException(HttpServletRequest request,
           InvalidChallengeStatusException e){
        return new ResponseEntity<>(new SingleErrorResponse(e.getError()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongChallengeEditorException.class)
    public ResponseEntity<Response> handleInvalidChallengeStatusException(HttpServletRequest request,
                                    WrongChallengeEditorException e){
        return new ResponseEntity<>(new SingleErrorResponse(e.getError()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChallengeIdNotSpecifiedException.class)
    public ResponseEntity<Response> handleChallengeIdNotSpecifiedException(HttpServletRequest request,
                                                                          ChallengeIdNotSpecifiedException e){
        return new ResponseEntity<>(new SingleErrorResponse("challenge.id.not.specified"),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KudosExceededException.class)
    public ResponseEntity<Response> handleKudosExceededException(HttpServletRequest request,
                                                                           KudosExceededException e){
        return new ResponseEntity<>(new SingleErrorResponse(e.getError()),HttpStatus.BAD_REQUEST);
    }

}
