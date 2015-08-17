package kudos.web.controllers;

import kudos.model.Challenge;
import kudos.model.User;
import kudos.web.beans.form.LoginForm;
import kudos.web.beans.response.UserResponse;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.SingleErrorResponse;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Created by chc on 15.8.10.
 */
@Api(name = "user authentication", description = "Login and logout for a user")
@Controller
public class AuthenticationController extends BaseController {

    @ApiMethod(description = "Service to log into system")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiErrors(apierrors =
            {@ApiError(code = "email_password_mismatch",
                    description = "If entered email or password does not exist in database"),
             @ApiError(code = "email.not.specified",
                    description = "If email was not specified"),
             @ApiError(code = "password.not.specified",
                    description = "If password was not specified"),
             @ApiError(code = "user.not.exist",
                    description = "If user does not exist"),
             @ApiError(code = "user.already.logged",
                    description = "If user is already logged")})
    public @ApiResponseObject @ResponseBody
    User challenge(@ApiQueryParam(name = "email", required = false,
            description = "The email of registered user. For testing purposes use test1@google.lt") String email,
                   @ApiQueryParam(name = "password", required = false,
            description = "The password of registered user. For testing purpose use google") String password,
                                   HttpServletRequest request) throws FormValidationException, UserException {
        return usersService.login(email,password,request);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<Response> logout(HttpSession session, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new SingleErrorResponse("not.logged"),HttpStatus.NOT_FOUND);
        }
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
