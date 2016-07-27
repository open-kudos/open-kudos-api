package kudos.web.controllers;

import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.Challenge;
import kudos.model.ChallengeStatus;
import kudos.model.User;
import kudos.web.beans.request.GiveChallengeForm;
import kudos.web.beans.response.ChallengeActions;
import kudos.web.beans.response.ChallengeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/challenge")
@RestController
public class ChallengeController extends BaseController {

    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public ChallengeResponse giveChallenge(@RequestBody GiveChallengeForm form) throws UserException, MessagingException,
            InvalidKudosAmountException {
        User creator = authenticationService.getLoggedInUser();
        Optional<User> receiver = usersService.findByEmail(form.getReceiverEmail().toLowerCase());

        if(receiver.isPresent()) {
            Challenge challenge = challengeService.giveChallenge(creator, receiver.get(), form.getName(),
                    form.getDescription(), form.getExpirationDate(), form.getAmount());
            emailService.sendEmailForNewChallenge(creator, receiver.get(), challenge);
            return new ChallengeResponse(challenge, getAllowedActions(creator, challenge));
        } else {
            String email = creator.getFirstName() + " " + creator.getLastName() + "wanted to give you CHALLENGE," +
                    " but you are not registered. Maybe it is time to do it? Go to www.openkudos.com and try it!";
            emailService.sendEmail(form.getReceiverEmail().toLowerCase(), email, "Open Kudos");
            throw new UserException("receiver_does_not_exist");
        }
    }

    @RequestMapping(value = "/sentAndReceived", method = RequestMethod.GET)
    public Page<ChallengeResponse> sentAndReceived(@RequestParam(value="page") int page,
                                                   @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(challengeService.getAllSentAndReceivedChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/sentAndReceived/{userId}", method = RequestMethod.GET)
    public Page<ChallengeResponse> sentAndReceived(@PathVariable String userId,
                                                   @RequestParam(value="page") int page,
                                                   @RequestParam(value="size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(challengeService.getAllSentAndReceivedChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/ongoing", method = RequestMethod.GET)
    public Page<ChallengeResponse> ongoing(@RequestParam(value="page") int page,
                                           @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(challengeService.getAllOngoingChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/ongoing/{userId}", method = RequestMethod.GET)
    public Page<ChallengeResponse> ongoing(@PathVariable String userId,
                                           @RequestParam(value="page") int page,
                                           @RequestParam(value="size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(challengeService.getAllOngoingChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public Page<ChallengeResponse> history(@RequestParam(value="page") int page,
                                           @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(challengeService.getAllFailedAndAccomplishedChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/{userId}", method = RequestMethod.GET)
    public Page<ChallengeResponse> history(@PathVariable String userId,
                                           @RequestParam(value="page") int page,
                                           @RequestParam(value="size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(challengeService.getAllFailedAndAccomplishedChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/failed", method = RequestMethod.GET)
    public Page<ChallengeResponse> historyForFailed(@RequestParam(value="page") int page,
                                                    @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(challengeService.getAllFailedChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/failed/{userId}", method = RequestMethod.GET)
    public Page<ChallengeResponse> historyForFailed(@PathVariable String userId,
                                                     @RequestParam(value="page") int page,
                                                     @RequestParam(value="size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(challengeService.getAllFailedChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/accomplished", method = RequestMethod.GET)
    public Page<ChallengeResponse> historyForAccomplished(@RequestParam(value="page") int page,
                                                          @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(challengeService.getAllAccomplishedChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/accomplished/{userId}", method = RequestMethod.GET)
    public Page<ChallengeResponse> historyForAccomplished(@PathVariable String userId,
                                                          @RequestParam(value="page") int page,
                                                          @RequestParam(value="size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(challengeService.getAllAccomplishedChallenges(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/accept/{challengeId}", method = RequestMethod.POST)
    public ChallengeResponse accept(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challengeToAccept = challengeService.getChallengeById(challengeId);
        Challenge challengeToReturn = challengeService.acceptChallenge(challengeToAccept, user);
        return new ChallengeResponse(challengeToReturn, getAllowedActions(user, challengeToReturn));
    }

    @RequestMapping(value = "/decline/{challengeId}", method = RequestMethod.POST)
    public void decline(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challengeService.declineChallenge(challenge, user);
    }

    @RequestMapping(value = "/cancel/{challengeId}", method = RequestMethod.POST)
    public void cancel(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challengeService.cancelChallenge(challenge, user);
    }

    @RequestMapping(value = "/markAsCompleted/{challengeId}", method = RequestMethod.POST)
    public void markAsCompleted(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challengeService.markChallengeAsCompleted(challenge, user);
    }

    @RequestMapping(value = "/markAsFailed/{challengeId}", method = RequestMethod.POST)
    public void markAsFailed(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challengeService.markChallengeAsFailed(challenge, user);
    }

    public Page<ChallengeResponse> convert(Page<Challenge> challenges, User user) throws UserException {
        List<ChallengeResponse> response = new ArrayList<>();

        for(Challenge item : challenges.getContent()) {
            response.add(new ChallengeResponse(item, getAllowedActions(user, item)));
        }
        return new PageImpl<>(response, new PageRequest(challenges.getNumber(), challenges.getSize()),
                challenges.getTotalElements());
    }

    public ChallengeActions getAllowedActions(User user, Challenge challenge) throws UserException {
        if(challenge.getCreator().getId().contentEquals(user.getId())) {
            return getActionsForCreator(challenge.getStatus());
        } else if(challenge.getParticipant().getId().contentEquals(user.getId())) {
            return getActionsForParticipant(challenge.getStatus());
        } else {
            throw new UserException("no_supported_challenge_actions");
        }
    }

    private ChallengeActions getActionsForCreator(ChallengeStatus status) {
        switch (status) {
            case CREATED:
                return new ChallengeActions(true, false, false, false, false);
            case ACCEPTED:
                return new ChallengeActions(false, false, false, true, true);
            default:
                return new ChallengeActions(false, false, false, false, false);
        }
    }

    private ChallengeActions getActionsForParticipant(ChallengeStatus status) {
        switch (status) {
            case CREATED:
                return new ChallengeActions(false, true, true, false, false);
            default:
                return new ChallengeActions(false, false, false, false, false);
        }
    }

}
