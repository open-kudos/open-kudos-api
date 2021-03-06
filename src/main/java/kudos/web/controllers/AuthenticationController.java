package kudos.web.controllers;

import kudos.exceptions.FormValidationException;
import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.model.status.UserStatus;
import kudos.web.beans.request.LoginForm;
import kudos.web.beans.request.RegisterForm;
import kudos.web.beans.request.validator.BaseValidator;
import kudos.web.beans.request.validator.LoginFormValidator;
import kudos.web.beans.request.validator.RegisterFormValidator;
import org.json.JSONObject;
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

    String serverUrl = "localhost:8080";

    @Autowired
    RegisterFormValidator registerFormValidator;

    @Autowired
    LoginFormValidator loginFormValidator;

    @Autowired
    BaseValidator baseValidator;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody RegisterForm form, BindingResult errors) throws MessagingException, UserException, FormValidationException {
        registerFormValidator.validate(form, errors);

        if(errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        authenticationService.registerUser(new User(form.getFirstName(), form.getLastName(),
                form.getPassword(), form.getEmail().toLowerCase(), UserStatus.NOT_CONFIRMED));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody LoginForm form, HttpServletRequest request, BindingResult errors) throws UserException, FormValidationException {
        loginFormValidator.validate(form, errors);
        if(errors.hasErrors())
            throw new FormValidationException(errors);
        authenticationService.login(form.getEmail().toLowerCase(), form.getPassword(), request);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpSession session, Principal principal) throws UserException {
        authenticationService.logout(session, principal);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void resetPassword(@RequestBody String email) throws UserException {
        JSONObject obj = new JSONObject(email);
        if(baseValidator.isEmailWrongPattern(obj.getString("email"))) {
            throw new UserException("email_incorrect_pattern");
        }
        String newPassword = authenticationService.resetPassword(obj.getString("email"));
        String emailMessage = "Your new password: " + "<b>" + newPassword  + "</b> <br> You can change your password in settings";
        emailService.sendEmail(obj.getString("email"), emailMessage, "Open Kudos");
    }

    @RequestMapping(value = "/change/password", method = RequestMethod.POST)
    public void changePassword(@RequestBody String newPassword) throws UserException {
        JSONObject obj = new JSONObject(newPassword);
        authenticationService.changePassword(obj.getString("newPassword"));
    }

}
