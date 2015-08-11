package kudos.web.controllers;

import kudos.web.beans.form.LoginForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.SingleErrorResponse;
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
                                   Principal principal, HttpServletRequest request) throws FormValidationException {
        new LoginForm.LoginFormValidator().validate(loginForm,errors);

        if(errors.hasErrors()){
            throw new FormValidationException(errors);
        } else if(principal == null) {

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword());

            try {
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (AuthenticationCredentialsNotFoundException e) {
                return new ResponseEntity<>(new SingleErrorResponse("user.not.found"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new SingleErrorResponse("already.logged"), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<Response> logout(HttpSession session, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new SingleErrorResponse("already.logged"),HttpStatus.NOT_FOUND);
        }
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
