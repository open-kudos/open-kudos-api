package kudos.web.controllers;

import kudos.web.beans.form.UserForm;
import kudos.web.beans.response.UserResponse;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * Created by chc on 15.8.10.
 */
@Controller
public class RegistrationController extends BaseController {

    private Logger LOG = Logger.getLogger(RegistrationController.class);

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<Response> register(@ModelAttribute("form") UserForm userForm, Errors errors) throws FormValidationException, MessagingException, UserException {
        new UserForm.FormValidator().validate(userForm, errors);
        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        return new ResponseEntity<>(new UserResponse(usersService.registerUser(userForm.toUser())), HttpStatus.CREATED);

    }

    @RequestMapping(value = "/confirm-email", method = RequestMethod.GET)
    public ResponseEntity<Response> confirmEmail(@RequestParam String hashedMail) throws UserException {
        return new ResponseEntity<>(new UserResponse(usersService.confirmUser(hashedMail)),HttpStatus.OK);
    }


}
