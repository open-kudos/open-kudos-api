package kudos.web.controllers;

import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.Challenge;
import kudos.model.ChallengeStatus;
import kudos.model.User;
import kudos.web.beans.request.GiveChallengeForm;
import kudos.web.beans.response.ChallengeActions;
import kudos.web.beans.response.ChallengeResponse;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/challenge")
@RestController
public class ChallengeController extends BaseController {

    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public void giveChallenge(@RequestBody GiveChallengeForm form) throws UserException, MessagingException, InvalidKudosAmountException {
        User creator = authenticationService.getLoggedInUser();
        Optional<User> receiver = usersService.findByEmail(form.getReceiverEmail().toLowerCase());

        if(receiver.isPresent()) {
            Challenge challenge = challengeService.giveChallenge(creator, receiver.get(), form.getName(),
                    form.getDescription(), form.getExpirationDate(), form.getAmount());
            emailService.sendEmailForNewChallenge(creator, receiver.get(), challenge);
        } else {
            String email = creator.getFirstName() + " " + creator.getLastName() + "wanted to give you CHALLENGE," +
                    " but you are not registered. Maybe it is time to do it? Go to www.openkudos.com and try it!";
            emailService.sendEmail(form.getReceiverEmail().toLowerCase(), email, "Open Kudos");
            throw new UserException("receiver_does_not_exist");
        }
    }

    @RequestMapping(value = "/sentAndReceived", method = RequestMethod.GET)
    public List<ChallengeResponse> sentAndReceived() throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(challengeService.getAllSentAndReceivedChallenges(user), user);
    }

    @RequestMapping(value = "/ongoing", method = RequestMethod.GET)
    public List<ChallengeResponse> ongoing() throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(challengeService.getAllOngoingChallenges(user), user);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<ChallengeResponse> history() throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(challengeService.getAllFailedAndCompletedChallenges(user), user);
    }

    @RequestMapping(value = "/accept/{challengeId}", method = RequestMethod.POST)
    public void accept(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challengeService.acceptChallenge(challenge, user);
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

    public List<ChallengeResponse> convert(List<Challenge> challenges, User user) throws UserException {
        List<ChallengeResponse> response = new ArrayList<>();

        for(Challenge item : challenges) {
            ChallengeResponse result = new ChallengeResponse(item);
            result.setActions(getAllowedActions(user, item));
            response.add(result);
        }
        return response;
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
