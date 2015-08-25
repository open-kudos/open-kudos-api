package kudos.web.controllers;

import kudos.model.User;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Created by chc on 15.8.10.
 */
@Api(name = "user authentication", description = "Login and logout for a user. For testing purposes use test1@google.lt with password google")
@Controller
public class AuthenticationController extends BaseController {

    @ApiMethod(description = "Service to log into system")
    @ApiErrors(apierrors = {
             @ApiError(code = "email_password_mismatch", description = "If entered email or password does not exist in database"),
             @ApiError(code = "email_not_specified", description = "If email was not specified"),
             @ApiError(code = "password_not_specified", description = "If password was not specified"),
             @ApiError(code = "user_not_exist", description = "If user does not exist"),
             @ApiError(code = "user_already_logged", description = "If user is already logged")})
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody User login(
                @ApiQueryParam(name = "email") String email,
                @ApiQueryParam(name = "password") String password,
           HttpServletRequest request) throws FormValidationException, UserException {
        return usersService.login(email,password,request);
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
