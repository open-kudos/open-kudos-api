package kudos.web.controllers;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import kudos.exceptions.BusinessException;
import kudos.exceptions.ChallengeIdNotSpecifiedException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.exceptions.WrongChallengeEditorException;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.web.beans.form.ChallengeTransferForm;
import kudos.web.beans.response.ChallengeHistoryResponse;
import kudos.web.beans.response.ChallengeResponse;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.SingleErrorResponse;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.joda.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;

/**
 * Created by chc on 15.8.11.
 */
@RequestMapping("/challenges")
@Controller
public class ChallengeController extends BaseController {

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Response> challenge(@ModelAttribute("form") ChallengeTransferForm form, Errors errors)
            throws FormValidationException, ParseException, BusinessException, UserException {

        new ChallengeTransferForm.ChallengeTransferFormValidator().validate(form, errors);

        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        Optional<User> participant = usersService.findByEmail(form.getParticipant());
        Optional<User> referee = usersService.findByEmail(form.getReferee());

        if(!participant.isPresent()) {
            return new ResponseEntity<>(new SingleErrorResponse("participant.not.exist"),HttpStatus.BAD_REQUEST);
        }

        if(!referee.isPresent()){
            return new ResponseEntity<>(new SingleErrorResponse("referee.not.exist"),HttpStatus.BAD_REQUEST);
        }

        LocalDateTime due = formatter.parseLocalDateTime(form.getFinishDate());

        int amount = Integer.parseInt(form.getAmount());

        Challenge challenge = challengeService.create(participant.get(), referee.get(), form.getName(), due, amount);

        return new ResponseEntity<>(new ChallengeResponse(challenge),HttpStatus.OK);

    }

    @RequestMapping(value = "/get-created", method = RequestMethod.GET)
    public ResponseEntity<Response> createdChallenges() throws UserException {
        return new ResponseEntity<>(new ChallengeHistoryResponse(challengeService.getAllUserCreatedChallenges()),HttpStatus.OK);
    }

    @RequestMapping(value = "/get-participated", method = RequestMethod.GET)
    public ResponseEntity<Response> participatedChallenges() throws UserException {
        return new ResponseEntity<>(new ChallengeHistoryResponse(challengeService.getAllUserParticipatedChallenges()),HttpStatus.OK);
    }

    @RequestMapping(value = "/get-referred", method = RequestMethod.GET)
    public ResponseEntity<Response> refferedChallenges() throws UserException {
        return new ResponseEntity<>(new ChallengeHistoryResponse(challengeService.getAllUserReferredChallenges()),HttpStatus.OK);
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public ResponseEntity<Response> accept(String id) throws InvalidChallengeStatusException, WrongChallengeEditorException, ChallengeIdNotSpecifiedException, UserException {

        if(Strings.isNullOrEmpty(id)){
            throw new ChallengeIdNotSpecifiedException();
        }

        Challenge challenge = challengeService.getChallenge(id);
        if(!challenge.getParticipant().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not.a.participant");
        }
        return new ResponseEntity<>(new ChallengeResponse(challengeService.accept(challenge)),HttpStatus.OK);
    }

    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    public ResponseEntity<Response> decline(String id) throws BusinessException, ChallengeIdNotSpecifiedException, UserException {

        if(Strings.isNullOrEmpty(id)){
            throw new ChallengeIdNotSpecifiedException();
        }
        Challenge challenge = challengeService.getChallenge(id);
        if(!challenge.getParticipant().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not.a.participant");
        }
        return new ResponseEntity<>(new ChallengeResponse(challengeService.decline(challenge)),HttpStatus.OK);
    }

    @RequestMapping(value = "/accomplish", method = RequestMethod.POST)
    public ResponseEntity<Response> accomplish(String id) throws BusinessException, ChallengeIdNotSpecifiedException, UserException {

        if(Strings.isNullOrEmpty(id)){
            throw new ChallengeIdNotSpecifiedException();
        }

        Challenge challenge = challengeService.getChallenge(id);
        if(!challenge.getReferee().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not.a.referee");
        }
        return new ResponseEntity<>(new ChallengeResponse(challengeService.accomplish(challenge)),HttpStatus.OK);
    }

    @RequestMapping(value = "/fail", method = RequestMethod.POST)
    public ResponseEntity<Response> fail(String id) throws BusinessException, ChallengeIdNotSpecifiedException, UserException {

        if(Strings.isNullOrEmpty(id)){
            throw new ChallengeIdNotSpecifiedException();
        }

        Challenge challenge = challengeService.getChallenge(id);
        if(!challenge.getReferee().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not.a.referee");
        }
        return new ResponseEntity<>(new ChallengeResponse(challengeService.fail(challenge)),HttpStatus.OK);
    }

}
