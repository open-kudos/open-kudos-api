package kudos.web.controllers;

import kudos.exceptions.FormValidationException;
import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.model.UserStatus;
import kudos.web.beans.request.validator.RegisterFormValidator;
import kudos.web.beans.request.LoginForm;
import kudos.web.beans.request.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController extends BaseController {

    @Autowired
    RegisterFormValidator validator;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody RegisterForm form, BindingResult errors) throws MessagingException, UserException, FormValidationException {
        validator.validate(form, errors);
        if(errors.hasErrors())
            throw new FormValidationException(errors);

        User user = authenticationService.registerUser(new User(form.getFirstName(), form.getLastName(),
                form.getPassword(), form.getEmail().toLowerCase(), UserStatus.NOT_CONFIRMED));

        String message = "Your confirmation code is : <b>" + user.getEmailHash() + "</b>";
        emailService.sendEmail(user.getEmail(), message, "Greetings from Acorns app");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody LoginForm form, HttpServletRequest request) throws UserException {
        authenticationService.login(form.getEmail().toLowerCase(), form.getPassword(), request);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpSession session, Principal principal) throws UserException {
        authenticationService.logout(session, principal);
    }

    @RequestMapping(value = "/confirm/{confirmationCode}", method = RequestMethod.POST)
    public void confirmRegistration(@PathVariable String confirmationCode) throws UserException {
        authenticationService.confirmRegistration(confirmationCode);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void resetPassword(@RequestBody String email) throws UserException, MessagingException {
        String newPassword = authenticationService.resetPassword(email);
        String emailMessage = "Your new password: " + "<b>" + newPassword  + "</b> <br> You can change your password in settings";
        String subject = "OpenKudos new password";
        emailService.sendEmailOnNewThread(email, emailMessage, subject);
    }

    @RequestMapping(value = "/change/password", method = RequestMethod.POST)
    public void changePassword(@RequestBody String newPassword) throws UserException {
        authenticationService.changePassword(newPassword);
    }

}
