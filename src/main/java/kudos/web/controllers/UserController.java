package kudos.web.controllers;

import kudos.web.beans.form.MyProfileForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.*;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


/**
 * Created by chc on 15.7.23.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private static Logger LOG = Logger.getLogger(UserController.class.getName());


    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public ResponseEntity<Response> disableMyAccount(HttpSession session) {
        usersService.disableMyAcount();
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ResponseEntity<Response> showHomePage() throws UserException {
        return new ResponseEntity<>(new UserResponse(usersService.getCompletedUser()),HttpStatus.OK);
    }

    @RequestMapping(value = "complete-profile", method = RequestMethod.POST)
    public ResponseEntity<Response> completeUserProfile(@ModelAttribute("form") MyProfileForm myProfileForm, Errors errors) throws FormValidationException {
        new MyProfileForm.MyProfileValidator().validate(myProfileForm, errors);
        if(errors.hasErrors()){
            throw new FormValidationException(errors);
        }
        return new ResponseEntity<>(new UserResponse(usersService.completeUser(myProfileForm)),HttpStatus.OK);
    }
}
