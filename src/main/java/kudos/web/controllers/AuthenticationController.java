package kudos.web.controllers;

import kudos.web.beans.form.LoginForm;
import kudos.web.beans.response.UserResponse;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Api(name = "Authentication Controller", description = "Login and logout for a user. For testing purposes use test1@google.lt with password google")
@Controller
public class AuthenticationController extends BaseController {
    @Value("${kudos.domain}")
    private String domain;
    @ApiMethod(description = "Service to log into system")
    @ApiErrors(apierrors = {
            @ApiError(code = "email_password_mismatch", description = "If entered email or password does not exist in database"),
            @ApiError(code = "email_not_specified", description = "If email was not specified"),
            @ApiError(code = "password_not_specified", description = "If password was not specified"),
            @ApiError(code = "user_not_exist", description = "If user does not exist"),
            @ApiError(code = "user_already_logged", description = "If user is already logged")})
    @RequestMapping(value = "/login", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    public @ApiResponseObject @ResponseBody
    UserResponse login(@RequestBody LoginForm loginForm, Errors errors, HttpServletRequest request) throws FormValidationException, UserException {
        new LoginForm.LoginFormValidator(domain).validate(loginForm,errors);
        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }
        return usersService.login(loginForm.getEmail(), loginForm.getPassword(), request);
    }

    @ApiMethod(description = "Service to log out of system")
    @ApiErrors(apierrors = {
            @ApiError(code = "not_logged",
                    description = "If user is not logged in")
    })
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpSession session, Principal principal) throws UserException {
        if (principal == null) {
            throw new UserException("not_logged");
        }
        session.invalidate();
    }

}
