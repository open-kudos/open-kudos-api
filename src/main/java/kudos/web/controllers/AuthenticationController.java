package kudos.web.controllers;

import kudos.web.beans.form.LoginForm;
import kudos.web.beans.response.UserResponse;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.SingleErrorResponse;
import kudos.web.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Created by chc on 15.8.10.
 */
@Controller
public class AuthenticationController extends BaseController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Response> login(@ModelAttribute("form")LoginForm loginForm, Errors errors,
                                   Principal principal, HttpServletRequest request) throws FormValidationException, UserException {
        new LoginForm.LoginFormValidator().validate(loginForm,errors);

        if(errors.hasErrors()){
            throw new FormValidationException(errors);
        }

        if(usersService.getLoggedUser().isPresent()) {
            throw new UserException("user.already.logged");
        }
        return new ResponseEntity<>(new UserResponse(usersService.login(loginForm.getEmail(),
                loginForm.getPassword(),request)),HttpStatus.OK);
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
