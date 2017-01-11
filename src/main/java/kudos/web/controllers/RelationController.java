package kudos.web.controllers;

import kudos.exceptions.RelationException;
import kudos.exceptions.UserException;
import kudos.model.status.ActionType;
import kudos.model.Relation;
import kudos.model.User;
import kudos.web.beans.response.RelationResponse;
import kudos.web.beans.response.userActionResponse.UserAction;
import org.jsondoc.core.annotation.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(description = "service to manage user relations", name = "Relation Controller")
@RestController
@RequestMapping("/relation")
public class RelationController extends BaseController {

    @RequestMapping(value = "/follow/{userId}", method = RequestMethod.POST)
    public void followById(@PathVariable String userId) throws UserException, RelationException {
        User follower = authenticationService.getLoggedInUser();
        User userToFollow = usersService.findByUserId(userId);
        relationService.follow(follower, userToFollow);
        actionsService.save(follower, userToFollow, ActionType.STARTED_TO_FOLLOW);
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public void followByEmail(@RequestParam String userEmail) throws UserException, RelationException, MessagingException {
        User follower = authenticationService.getLoggedInUser();
        Optional<User> userToFollow = usersService.findByEmail(userEmail.toLowerCase());

        if(userToFollow.isPresent()) {
            relationService.follow(follower, userToFollow.get());
            actionsService.save(follower, userToFollow.get(), ActionType.STARTED_TO_FOLLOW);
        } else {
//            String email = follower.getFirstName() + " " + follower.getLastName() + "wanted to add you to his" +
//                    " followed users list, but you are not registered. Maybe it is time to do it? Go to" +
//                    " www.openkudos.com and try it!";
//            emailService.sendEmail(userEmail.toLowerCase(), email, "Open Kudos");
            throw new UserException("receiver_does_not_exist");
        }
    }

    @RequestMapping(value = "/unfollow/{userId}", method = RequestMethod.POST)
    public void unfollow(@PathVariable String userId) throws UserException, RelationException {
        User follower = authenticationService.getLoggedInUser();
        User userToUnfollow = usersService.findByUserId(userId);
        actionsService.remove(follower, userToUnfollow);
        relationService.unfollow(follower, userToUnfollow);
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public Page<RelationResponse> getFollowers(@RequestParam(value="page") int page,
                                               @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(relationService.getUsersWhoFollowUser(user, new PageRequest(page, size)), true);
    }

    @RequestMapping(value = "/following", method = RequestMethod.GET)
    public Page<RelationResponse> getFollowing(@RequestParam(value="page") int page,
                                               @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(relationService.getUsersFollowedByUser(user, new PageRequest(page, size)), false);
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public Page<UserAction> getFollowedUsersFeed(@RequestParam(value="page") int page,
                                                 @RequestParam(value="size") int size) throws UserException{
        User user = authenticationService.getLoggedInUser();
        return actionConverter.convertActionsPage(actionsService.getFeedPage(new PageRequest(page, size, new Sort(Sort.Direction.DESC, "timestamp")), user));
    }

    public Page<RelationResponse> convert(Page<Relation> relations, boolean followers) throws UserException {
        List<RelationResponse> response = new ArrayList<>();

        for(Relation item : relations.getContent()) {
            User user;
            if(followers) {
                user = item.getFollower();
            } else {
                user = item.getUserToFollow();
            }
            response.add(new RelationResponse(user.getId(), user.getFirstName() + " " + user.getLastName(),
                    user.getEmail()));
        }
        return new PageImpl<>(response, new PageRequest(relations.getNumber(), relations.getSize()),
                relations.getTotalElements());
    }

}
