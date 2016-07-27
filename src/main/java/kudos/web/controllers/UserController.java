package kudos.web.controllers;

import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.web.beans.request.ProfileForm;
import kudos.web.beans.response.UserResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

}
