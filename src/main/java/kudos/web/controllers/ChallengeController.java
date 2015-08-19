package kudos.web.controllers;

import com.google.common.base.Strings;
import kudos.exceptions.BusinessException;
import kudos.exceptions.ChallengeIdNotSpecifiedException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.exceptions.WrongChallengeEditorException;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.web.beans.form.ChallengeTransferForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

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

        // TODO same question as in KudosController
        new ChallengeTransferForm.ChallengeTransferFormValidator().validate(form, errors);

        if (errors.hasErrors())
            throw new FormValidationException(errors);

        User participant = usersService.findByEmail(form.getParticipant()).orElseThrow(() -> new UserException("participant.not.exist"));
        User referee = usersService.findByEmail(form.getReferee()).orElseThrow(() -> new UserException("referee.not.exist"));

        return challengeService.create(
                participant,
                referee,
                form.getName(),
                formatter.parseLocalDateTime(form.getFinishDate()),
                Integer.parseInt(form.getAmount())
        );
    }

    @ApiMethod(description = "Gets all challenges that logged user has created")
    @RequestMapping(value = "/created", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> createdChallenges() throws UserException {
        return challengeService.getAllUserCreatedChallenges();
    }

    @ApiMethod(description = "Gets all challenges that logged user has participated")
    @RequestMapping(value = "/participated", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> participatedChallenges() throws UserException {
        return challengeService.getAllUserParticipatedChallenges();
    }

    @ApiMethod(description = "Gets all challenges that logged user has referred")
    @RequestMapping(value = "/referred", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> refferedChallenges() throws UserException {
        return challengeService.getAllUserReferredChallenges();
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
    public @ApiResponseObject @ResponseBody Challenge accept(String id)
            throws InvalidChallengeStatusException, WrongChallengeEditorException, ChallengeIdNotSpecifiedException, UserException {

        if(Strings.isNullOrEmpty(id))
            throw new ChallengeIdNotSpecifiedException();

        // TODO why getChallenge doesn't return Optional? Return optional and use "orElseThrow" instead of IF
        Challenge challenge = challengeService.getChallenge(id);
        if(!challenge.getParticipant().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not.a.participant");
        }
        return challengeService.accept(challenge);
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
    public @ApiResponseObject @ResponseBody Challenge decline(String id) throws BusinessException, ChallengeIdNotSpecifiedException, UserException {

        if(Strings.isNullOrEmpty(id))
            throw new ChallengeIdNotSpecifiedException();

        // TODO as above. check that challenge is not null (with Optional)
        Challenge challenge = challengeService.getChallenge(id);
        if(!challenge.getParticipant().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not.a.participant");
        }
        return challengeService.decline(challenge);
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
    public @ApiResponseObject @ResponseBody Challenge accomplish(String id) throws BusinessException, ChallengeIdNotSpecifiedException, UserException {

        if(Strings.isNullOrEmpty(id))
            throw new ChallengeIdNotSpecifiedException();

        // TODO as above. check that challenge is not null (with Optional)
        Challenge challenge = challengeService.getChallenge(id);
        if(!challenge.getReferee().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not.a.referee");
        }
        return challengeService.accomplish(challenge);
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
    public @ApiResponseObject @ResponseBody Challenge fail(String id) throws BusinessException, ChallengeIdNotSpecifiedException, UserException {

        if(Strings.isNullOrEmpty(id))
            throw new ChallengeIdNotSpecifiedException();

        // TODO as above. check that challenge is not null (with Optional)
        Challenge challenge = challengeService.getChallenge(id);
        if(!challenge.getReferee().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not.a.referee");
        }
        return challengeService.fail(challenge);
    }

}
