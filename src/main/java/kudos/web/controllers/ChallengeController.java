package kudos.web.controllers;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import kudos.exceptions.BusinessException;
import kudos.exceptions.ChallengeIdNotSpecifiedException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.exceptions.WrongChallengeEditorException;
import kudos.web.beans.form.ChallengeTransferForm;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.web.beans.response.ChallengeHistoryResponse;
import kudos.web.beans.response.ChallengeResponse;
import kudos.web.beans.response.SingleErrorResponse;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import kudos.web.exceptions.UserException;
import org.joda.time.LocalDateTime;
import org.jsondoc.core.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * Created by chc on 15.8.11.
 */
@Api(name = "challenge controller", description = "Controller for managing challenges")
@RequestMapping("/challenges")
@Controller
public class ChallengeController extends BaseController {

    @ApiMethod(description = "Service to create challenges")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "participant",
                        description = "The email of challenge participant. For testing use testP@google.lt"),
            @ApiQueryParam(name = "referee",
                        description = "The email of challenge referee, For testing use testR@google.lt"),
            @ApiQueryParam(name = "name",
                        description = "The name of challenge. For testing use XYZ"),
            @ApiQueryParam(name = "finishDate",
                        description = "Finish date until challenge must be completed. For testing use 2015-10-10 2015:15:15,333"),
            @ApiQueryParam(name = "amount",
                        description = "The amount of Kudos that will be gifted if challenge was completed. For testing use 10")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "receiver.email.not.specified",
                    description = "If receiver email was not specified"),
            @ApiError(code = "receiver.email.incorrect",
                    description = "If receiver email was incorrect"),
            @ApiError(code = "referee.email.not.specified",
                    description = "If referee email was not specified"),
            @ApiError(code = "referee.email.incorrect",
                    description = "If referee email was incorrect"),
            @ApiError(code = "amount.not.specified",
                    description = "If amount was not specified"),
            @ApiError(code = "amount.negative.or.zero",
                    description = "If specified amount was negative or equal to zero"),
            @ApiError(code = "amount.not.digit",
                    description = "If specified amount was not a digit"),
            @ApiError(code = "name.not.specified",
                    description = "If name was not specified"),
            @ApiError(code = "finishDate.not.specified",
                    description = "If finish date was not specified"),
            @ApiError(code = "finishDate.incorrect",
                    description = "If finishDate was incorrect"),
            @ApiError(code = "invalid.kudos.amount",
                    description = "If user does not have enough KUDOS to spent"),
            @ApiError(code = "receiver.not.exist",
                    description = "If kudos receiver does not exist"),
            @ApiError(code = "participant.not.exist",
                    description = "If participant user does not exist"),
            @ApiError(code = "referee.not.exist",
                    description = "If referee user does not exist")
    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody Challenge challenge(ChallengeTransferForm form, Errors errors)
            throws FormValidationException, ParseException, BusinessException, UserException {

        new ChallengeTransferForm.ChallengeTransferFormValidator().validate(form, errors);

        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        Optional<User> participant = usersService.findByEmail(form.getParticipant());
        Optional<User> referee = usersService.findByEmail(form.getReferee());

        if(!participant.isPresent()){
            throw new UserException("participant.not.exist");
        }

        if(!referee.isPresent()){
            throw new UserException("referee.not.exist");
        }
        return challengeService.create(participant.get(),referee.get(),form.getName(),
                formatter.parseLocalDateTime(form.getFinishDate()),
                Integer.parseInt(form.getAmount()));

    }

    @ApiMethod(description = "Gets all challenges that logged user has created")
    @RequestMapping(value = "/created", method = RequestMethod.GET)
    public ResponseEntity<Response> createdChallenges() throws UserException {
        return new ResponseEntity<>(new ChallengeHistoryResponse(challengeService.getAllUserCreatedChallenges()),HttpStatus.OK);
    }

    @ApiMethod(description = "Gets all challenges that logged user has participated")
    @RequestMapping(value = "/participated", method = RequestMethod.GET)
    public ResponseEntity<Response> participatedChallenges() throws UserException {
        return new ResponseEntity<>(new ChallengeHistoryResponse(challengeService.getAllUserParticipatedChallenges()),HttpStatus.OK);
    }

    @ApiMethod(description = "Gets all challenges that logged user has referred")
    @RequestMapping(value = "/referred", method = RequestMethod.GET)
    public ResponseEntity<Response> refferedChallenges() throws UserException {
        return new ResponseEntity<>(new ChallengeHistoryResponse(challengeService.getAllUserReferredChallenges()), HttpStatus.OK);
    }

    @ApiMethod(description = "Accepts challenge by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge.id.not.specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not.a.participant",
                    description = "If user is not a participant"),
            @ApiError(code = "challenge.already.accepted",
                    description = "If challenge is already accepted"),
            @ApiError(code = "challenge.already.accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge.already.declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge.already.failed",
                    description = "If challenge is already failed")
    })
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

    @ApiMethod(description = "Declines challenge by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge.id.not.specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not.a.participant",
                    description = "If user is not referee"),
            @ApiError(code = "challenge.already.accepted",
                    description = "If challenge is already accepted"),
            @ApiError(code = "challenge.already.accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge.already.declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge.already.failed",
                    description = "If challenge is already failed")
    })
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

    @ApiMethod(description = "Accomplishes challenge by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge.id.not.specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not.a.referee",
                    description = "If user is not a referee"),
            @ApiError(code = "challenge.already.accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge.already.declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge.already.failed",
                    description = "If challenge is already failed")
    })
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

    @ApiMethod(description = "Marks challenge as failed by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge.id.not.specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not.a.referee",
                    description = "If user is not a referee"),
            @ApiError(code = "challenge.already.accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge.already.declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge.already.failed",
                    description = "If challenge is already failed")
    })
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
