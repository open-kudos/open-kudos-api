package kudos.web.controllers;

import kudos.exceptions.FormValidationException;
import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.web.beans.request.ProfileForm;
import kudos.web.beans.request.validator.ProfileFormValidator;
import kudos.web.beans.response.UserResponse;
import kudos.web.beans.response.userActionResponse.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    ProfileFormValidator profileFormValidator;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public UserResponse getUserProfile() throws UserException {
        return new UserResponse(authenticationService.getLoggedInUser());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateUserProfile(@RequestBody ProfileForm form, BindingResult errors) throws UserException, FormValidationException {
        profileFormValidator.validate(form, errors);
        if(errors.hasErrors())
            throw new FormValidationException(errors);

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
        User currentUser = authenticationService.getLoggedInUser();
        return convert(usersService.getUserEmailPredicate(predicate, currentUser.getId()));
    }

    @RequestMapping(value = "/actions/{userId}", method = RequestMethod.GET)
    public Page<UserAction> getUserActions(@PathVariable String userId,
                                           @RequestParam(value="page") int page,
                                           @RequestParam(value="size") int size) throws UserException {
        return actionConverter.convertActionsPage(actionsService.getUserFeedPage(usersService.findByUserId(userId),
                                                    new PageRequest(page, size, new Sort(Sort.Direction.DESC, "timestamp"))));
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
