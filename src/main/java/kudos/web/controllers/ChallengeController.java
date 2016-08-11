package kudos.web.controllers;

import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.*;
import kudos.web.beans.request.AddCommentForm;
import kudos.web.beans.request.GiveChallengeForm;
import kudos.web.beans.response.ChallengeActions;
import kudos.web.beans.response.ChallengeResponse;
import kudos.web.beans.response.CommentResponse;
import org.joda.time.LocalDateTime;
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
            if (receiver.get().isSubscribing()) {
                emailService.sendEmailForNewChallenge(creator, receiver.get(), challenge);
            }
            actionsService.save(creator, challenge, ActionType.CREATED_CHALLENGE);
            return new ChallengeResponse(challenge, getAllowedActions(creator, challenge));
        } else {
            String email = creator.getFirstName() + " " + creator.getLastName() + "wanted to give you CHALLENGE," +
                    " but you are not registered. Maybe it is time to do it? Go to www.openkudos.com and try it!";
            emailService.sendEmail(form.getReceiverEmail().toLowerCase(), email, "Open Kudos");
            throw new UserException("receiver_does_not_exist_email_sent");
        }
    }

    @RequestMapping(value = "/get/{challengeId}", method = RequestMethod.GET)
    public ChallengeResponse sentAndReceived(@PathVariable String challengeId) throws UserException {
        return new ChallengeResponse(challengeService.getChallengeById(challengeId), new ChallengeActions(true, false, false, false, false));
    }


    @RequestMapping(value = "/{challengeId}/addComment", method = RequestMethod.POST)
    public void addCommentToChallenge(@PathVariable String challengeId,
                                      @RequestBody AddCommentForm form) throws UserException {
        User creator = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        Comment comment = new Comment(creator, form.getComment(), LocalDateTime.now().toString(), challenge);
        challengeService.addComment(comment);
        actionsService.save(creator, comment, ActionType.COMMENTED);
    }

    @RequestMapping(value = "/{challengeId}/comments", method = RequestMethod.GET)
    public Page<CommentResponse> getChallengeComments(@PathVariable String challengeId,
                                                      @RequestParam(value="page") int page,
                                                      @RequestParam(value="size") int size) throws UserException {
        Challenge challenge = challengeService.getChallengeById(challengeId);

        return convert(challengeService.getComments(challenge, new PageRequest(page, size)));
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

    @RequestMapping(value = "/{challengeId}/accept", method = RequestMethod.POST)
    public ChallengeResponse accept(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challengeToAccept = challengeService.getChallengeById(challengeId);
        Challenge challengeToReturn = challengeService.acceptChallenge(challengeToAccept, user);
        actionsService.save(user, challengeToReturn, ActionType.ACCEPTED_CHALLENGE);
        return new ChallengeResponse(challengeToReturn, getAllowedActions(user, challengeToReturn));
    }

    @RequestMapping(value = "/{challengeId}/decline", method = RequestMethod.POST)
    public void decline(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challengeService.declineChallenge(challenge, user);
    }

    @RequestMapping(value = "/{challengeId}/cancel", method = RequestMethod.POST)
    public void cancel(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        actionsService.remove(challenge);
        challengeService.cancelChallenge(challenge, user);
    }

    @RequestMapping(value = "/{challengeId}/markAsCompleted", method = RequestMethod.POST)
    public void markAsCompleted(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challengeService.markChallengeAsCompleted(challenge, user);
        actionsService.save(user, challenge, ActionType.MARKED_AS_COMPLETED);
    }

    @RequestMapping(value = "/{challengeId}/markAsFailed", method = RequestMethod.POST)
    public void markAsFailed(@PathVariable String challengeId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challengeService.markChallengeAsFailed(challenge, user);
        actionsService.save(user, challenge, ActionType.MARKED_AS_FAILED);
    }

    public Page<ChallengeResponse> convert(Page<Challenge> challenges, User user) throws UserException {
        List<ChallengeResponse> response = new ArrayList<>();

        for(Challenge item : challenges.getContent()) {
            response.add(new ChallengeResponse(item, getAllowedActions(user, item)));
        }
        return new PageImpl<>(response, new PageRequest(challenges.getNumber(), challenges.getSize()),
                challenges.getTotalElements());
    }

    public Page<CommentResponse> convert(Page<Comment> comments) throws UserException {
        List<CommentResponse> response = new ArrayList<>();

        for(Comment item : comments.getContent()) {
            response.add(new CommentResponse(item));
        }
        return new PageImpl<>(response, new PageRequest(comments.getNumber(), comments.getSize()),
                comments.getTotalElements());
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
