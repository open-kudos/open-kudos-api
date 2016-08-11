package kudos.web.controllers;

import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.web.beans.request.ProfileForm;
import kudos.web.beans.response.UserResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public UserResponse getUserProfile() throws UserException {
        return new UserResponse(authenticationService.getLoggedInUser());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateUserProfile(@RequestBody ProfileForm form) throws UserException {
        User user = authenticationService.getLoggedInUser();
        usersService.updateUserProfile(user, form.getFirstName(), form.getLastName(), form.getBirthday(),
                form.getStartedToWork());
    }

    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    public UserResponse getUserProfile(@PathVariable String userId) throws UserException {
        return new UserResponse(usersService.findByUserId(userId));
    }

    @RequestMapping(value = "/email/{predicate}", method = RequestMethod.GET)
    public List<UserResponse> getEmailPredicates(@PathVariable String predicate) throws UserException {
        return convert(usersService.getUserEmailPredicate(predicate));
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public void subscribeForEmailNotifications() throws UserException {
        User user = authenticationService.getLoggedInUser();
        usersService.subscribe(user);
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public void unsubscribeEmailNotifications() throws UserException {
        User user = authenticationService.getLoggedInUser();
        usersService.unsubscribe(user);
    }

    private List<UserResponse> convert(List<User> input) {
        List<UserResponse> users = new ArrayList<>();
        for(User user : input) {
            users.add(new UserResponse(user));
        }
        return users;
    }

}
