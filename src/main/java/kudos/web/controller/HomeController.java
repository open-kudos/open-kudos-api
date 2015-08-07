package kudos.web.controller;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.mongodb.MongoException;
import kudos.model.Email;
import kudos.model.User;
import kudos.model.UserForm;
import kudos.services.email.EmailServiceTestingPurposes;
import kudos.web.model.ErrorResponse;
import kudos.web.model.IndexResponse;
import kudos.web.model.DataResponse;
import kudos.web.model.Response;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
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
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Date;

/**
 * Created by chc on 15.7.29.
 */
@Controller
public class HomeController extends BaseController {

    Logger LOG = Logger.getLogger(HomeController.class.getName());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response index(Principal principal) {
        IndexResponse response = new IndexResponse();
        response.setIsLogged(principal != null);
        return response;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> login(String email,
                          String password, HttpServletRequest request, Principal principal) {

        if(principal == null) {

            if (Strings.isNullOrEmpty(email)) {
                return new ResponseEntity<>(DataResponse.fail("email.not.specified"), HttpStatus.BAD_REQUEST);
            }

            if (Strings.isNullOrEmpty(password)) {
                return new ResponseEntity<>(DataResponse.fail("password.not.specified"), HttpStatus.BAD_REQUEST);
            }

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

            try {
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            } catch (AuthenticationCredentialsNotFoundException e) {
                return new ResponseEntity<>(DataResponse.fail(e.getMessage()), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(DataResponse.success(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(DataResponse.fail("Please logout before logging in with new account"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> register(@ModelAttribute("form") UserForm userForm, Errors errors) {
        new UserForm.FormValidator().validate(userForm, errors);
        User user = userRepository.findOne(userForm.getEmail());
        boolean areErrors = errors.hasErrors();
        if (!areErrors && user == null) {

            try {
                StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
                new EmailServiceTestingPurposes().send(new Email(userForm.getEmail(), new Date().toString(), "confirmationMail",
                        "Welcome to KUDOS app. To verify your email, please paste this link to yout browser: localhost:8080/confirm-email?hashedMail="
                                + encryptor.encryptPassword(userForm.getEmail())));
                String oldPassword = userForm.getPassword();
                userForm.setPassword(new StrongPasswordEncryptor().encryptPassword(oldPassword));
                LOG.warn("saving user... ");
                userRepository.save(userForm.toUser());
            } catch(MessagingException e){
                    LOG.warn("There was an error with messages");
                    return new ResponseEntity<>(DataResponse.fail(e.getMessage() + "error with message"), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (MongoException e){
                    LOG.warn("there was an error with mongo");
                    return new ResponseEntity<>(DataResponse.fail(e.getMessage() + "error with mongo"),HttpStatus.INTERNAL_SERVER_ERROR);
            }
            LOG.warn("User was registered");

        } else if (!areErrors && user.isRegistered()) {
            LOG.warn("email already exists, fail");
            errors.rejectValue("email", "email.already.exists");
            return new ResponseEntity<>(ErrorResponse.create(errors.getFieldErrors()), HttpStatus.BAD_REQUEST);
        } else if(!errors.hasErrors() && !user.isRegistered()){
            userRepository.save(userForm.toUser());
            return new ResponseEntity<>(DataResponse.success("Welcome back to kudos!."), HttpStatus.OK);
        }

        return new ResponseEntity<>(DataResponse.success("Confirmation mail has been sent."), HttpStatus.OK);

    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<Response> logout(HttpSession session, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(DataResponse.fail("Cannot logout because you are not logged in"),HttpStatus.NOT_FOUND);
        }
        session.invalidate();
        return new ResponseEntity<>(DataResponse.success(),HttpStatus.OK);
    }

    @RequestMapping(value = "/confirm-email" , method = RequestMethod.GET)
    public ResponseEntity<Response> confirmEmail(@RequestParam String hashedMail){
        String email = new StrongTextEncryptor().decrypt("localhost:8080/confirm-email?hashedMail=b4pX21I30Yy8/jLsQG4v7vVlBpEb7tf8pK9X9S4CGL2RGRLfFHXkSwNz9cg1aWTf");
        User user = userRepository.findOne(email);
        if(user != null){
            user.markUserAsConfirmed();
            userRepository.save(user);
            return new ResponseEntity<>(DataResponse.success(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(DataResponse.fail("user with your confirmation link was not found"),HttpStatus.NOT_FOUND);
        }
    }

}