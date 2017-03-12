package kudos.web.controllers;

import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.services.AuthenticationService;
import kudos.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {


    @Autowired
    UsersService usersService;

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() throws UserException {
        return usersService.getAllUsers();
    }

    @RequestMapping(value = "/confirm/{userEmailHash}", method = RequestMethod.GET)
    public User confirmUser(@PathVariable String userEmailHash) throws UserException {
        return authenticationService.confirmUser(userEmailHash);
    }


}
