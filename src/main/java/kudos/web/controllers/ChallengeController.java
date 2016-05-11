package kudos.web.controllers;

import com.google.common.base.Strings;
import kudos.exceptions.*;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.services.ChallengeService;
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
import java.util.Optional;

/**
 * Created by chc on 15.8.11.
 */
@Api(name = "Challenge Controller", description = "Controller for managing challenges")
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
            @ApiError(code = "receiver_email_not_specified",
                    description = "If receiver email was not specified"),
            @ApiError(code = "receiver_email_incorrect",
                    description = "If receiver email was incorrect"),
            @ApiError(code = "referee_email_not_specified",
                    description = "If referee email was not specified"),
            @ApiError(code = "referee_email_incorrect",
                    description = "If referee email was incorrect"),
            @ApiError(code = "amount_not_specified",
                    description = "If amount was not specified"),
            @ApiError(code = "amount_negative_or_zero",
                    description = "If specified amount was negative or equal to zero"),
            @ApiError(code = "amount_not_digit",
                    description = "If specified amount was not a digit"),
            @ApiError(code = "name_not_specified",
                    description = "If name was not specified"),
            @ApiError(code = "finishDate_not_specified",
                    description = "If finish date was not specified"),
            @ApiError(code = "finishDate_incorrect",
                    description = "If finishDate was incorrect"),
            @ApiError(code = "invalid_kudos_amount",
                    description = "If user does not have enough KUDOS to spent"),
            @ApiError(code = "receiver_not_exist",
                    description = "If kudos receiver does not exist"),
            @ApiError(code = "participant_not_exist",
                    description = "If participant user does not exist"),
            @ApiError(code = "referee_not_exist",
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
        //User referee = usersService.findByEmail(form.getReferee()).orElseThrow(() -> new UserException("referee.not.exist"));

        return challengeService.create(
                participant,
                //referee,
                form.getName(),
                form.getDescription(),
                form.getFinishDate(),
                Integer.parseInt(form.getAmount())
        );
    }

    @ApiMethod(description = "Gets all challenges that logged user has created")
    @RequestMapping(value = "/created", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> createdChallenges() throws UserException {
        return challengeService.getAllUserCreatedChallenges();
    }

    @ApiMethod(description = "Gets all challenges that logged user has created by its status")
    @RequestMapping(value = "/createdByStatus", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> createdChallengesByStatus(Challenge.Status status) throws UserException {
        return challengeService.getAllUserCreatedChallengesByStatus(status);
    }

    @ApiMethod(description = "Gets all challenges that logged user has participated")
    @RequestMapping(value = "/participated", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> participatedChallenges() throws UserException {
        return challengeService.getAllUserParticipatedChallenges();
    }

    @ApiMethod(description = "Gets all challenges that logged user has participated by status")
    @RequestMapping(value = "/participatedByStatus", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> participatedChallengesByStatus(Challenge.Status status) throws UserException {
        return challengeService.getAllUserParticipatedChallengesByStatus(status);
    }

    @ApiMethod(description = "Gets all challenges that logged user has participated by status pageable")
    @RequestMapping(value = "/participatedByStatusPageable", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> participatedChallengesByStatusPageable(Challenge.Status status, int page, int pageSize) throws UserException {
        return challengeService.getAllUserParticipatedChallengesByStatusPageable(status, page, pageSize);
    }
/*
    @ApiMethod(description = "Gets all challenges that logged user has referred")
    @RequestMapping(value = "/referred", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> refferedChallenges() throws UserException {
        return challengeService.getAllUserReferredChallenges();
    }*/
/*
    @ApiMethod(description = "Gets all challenges that logged user has referred by status")
    @RequestMapping(value = "/referredByStatus", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Challenge> refferedChallengesByStatus(Challenge.Status status) throws UserException {
        return challengeService.getAllUserReferredChallengesByStatus(status);
    }*/

    @ApiMethod(description = "Accepts challenge by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge.id.not.specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not_a_participant",
                    description = "If user is not a participant"),
            @ApiError(code = "challenge_already_accepted",
                    description = "If challenge is already accepted"),
            @ApiError(code = "challenge_already_accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge_already_declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge_already_failed",
                    description = "If challenge is already failed"),
            @ApiError(code = "challenge_already_canceled",
                    description = "If challenge is already canceled")
    })
    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody Challenge accept(String id)
            throws BusinessException, IdNotSpecifiedException, ChallengeException, UserException {

        if(Strings.isNullOrEmpty(id))
            throw new IdNotSpecifiedException("id.not.specified");

        Optional<Challenge> maybeChallenge = challengeService.getChallenge(id);
        if(!maybeChallenge.isPresent()){
            throw new ChallengeException("challenge_not_found");
        }

        Challenge challenge = maybeChallenge.get();
        if(!challenge.getParticipant().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not_a_participant");
        }
        return challengeService.accept(challenge);
    }

    @ApiMethod(description = "Declines challenge by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge_id_not_specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not_a_participant",
                    description = "If user is not referee"),
            @ApiError(code = "challenge_already_accepted",
                    description = "If challenge is already accepted"),
            @ApiError(code = "challenge_already_accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge_already_declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge_already_failed",
                    description = "If challenge is already failed"),
            @ApiError(code = "challenge_already_canceled",
                    description = "If challenge is already canceled")
    })
    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody Challenge decline(String id) throws BusinessException, IdNotSpecifiedException, UserException, ChallengeException {

        if(Strings.isNullOrEmpty(id))
            throw new IdNotSpecifiedException("id_not_specified");

        Optional<Challenge> maybeChallenge = challengeService.getChallenge(id);

        if(!maybeChallenge.isPresent()){
            throw new ChallengeException("challenge_not_found");
        }

        Challenge challenge = maybeChallenge.get();
        if(!challenge.getParticipant().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not_a_participant");
        }
        return challengeService.decline(challenge);
    }

    @ApiMethod(description = "Cancels challenge by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge_id_not_specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not_a_creator",
                    description = "If user is not a creator"),
            @ApiError(code = "challenge_already_accepted",
                    description = "If challenge is already accepted"),
            @ApiError(code = "challenge_already_accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge_already_declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge_already_failed",
                    description = "If challenge is already failed"),
            @ApiError(code = "challenge_already_canceled",
                    description = "If challenge is already canceled")
    })
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody Challenge cancel(String id) throws BusinessException, IdNotSpecifiedException, UserException, ChallengeException {

        if(Strings.isNullOrEmpty(id))
            throw new IdNotSpecifiedException("id_not_specified");

        Optional<Challenge> maybeChallenge = challengeService.getChallenge(id);

        if(!maybeChallenge.isPresent()){
            throw new ChallengeException("challenge_not_found");
        }

        Challenge challenge = maybeChallenge.get();
        if(!challenge.getCreator().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not_a_creator");
        }
        return challengeService.cancel(challenge);
    }


    @ApiMethod(description = "Accomplishes challenge by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge_id_not_specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not_a_referee",
                    description = "If user is not a referee"),
            @ApiError(code = "challenge_already_accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge_already_declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge_already_failed",
                    description = "If challenge is already failed"),
            @ApiError(code = "challenge_already_canceled",
                    description = "If challenge is already canceled")
    })
    @RequestMapping(value = "/accomplish", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody void accomplish(String id, Boolean status) throws BusinessException, IdNotSpecifiedException, UserException, ChallengeException {

        if(Strings.isNullOrEmpty(id))
            throw new IdNotSpecifiedException("id_not_specified");

        Optional<Challenge> maybeChallenge = challengeService.getChallenge(id);
        if(!maybeChallenge.isPresent()){
            throw new ChallengeException("challenge_not_found");
        }

        Challenge challenge = maybeChallenge.get();
        if(challenge.getCreator().equals(usersService.getLoggedUser().get().getEmail())) {
            challenge.setCreatorStatus(status);

        }
        if (challenge.getParticipant().equals(usersService.getLoggedUser().get().getEmail())) {
            challenge.setParticipantStatus(status);
        }

        challengeService.accomplish(challenge);

//        if(!challenge.getReferee().equals(usersService.getLoggedUser().get().getEmail())) {
//            throw new WrongChallengeEditorException("not_a_referee");
//        }
    }

    @ApiMethod(description = "Marks challenge as failed by its id")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "challenge_id_not_specified",
                    description = "If challenge id was not specified"),
            @ApiError(code = "not_a_referee",
                    description = "If user is not a referee"),
            @ApiError(code = "challenge_already_accomplished",
                    description = "If challenge is already accomplished"),
            @ApiError(code = "challenge_already_declined",
                    description = "If challenge is already declined"),
            @ApiError(code = "challenge_already_failed",
                    description = "If challenge is already failed"),
            @ApiError(code = "challenge_already_canceled",
                    description = "If challenge is already canceled")
    })
    @RequestMapping(value = "/fail", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody Challenge fail(String id) throws BusinessException, IdNotSpecifiedException, UserException, ChallengeException {

        if(Strings.isNullOrEmpty(id))
            throw new IdNotSpecifiedException("id_not_specified");

        Optional<Challenge> maybeChallenge = challengeService.getChallenge(id);
        if(!maybeChallenge.isPresent()){
            throw new ChallengeException("challenge_not_found");
        }

        Challenge challenge = maybeChallenge.get();/*
        if(!challenge.getReferee().equals(usersService.getLoggedUser().get().getEmail())) {
            throw new WrongChallengeEditorException("not_a_referee");
        }*/
        return challengeService.fail(challenge);
    }

}
