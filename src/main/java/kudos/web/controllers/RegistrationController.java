package kudos.web.controllers;

import com.mongodb.MongoException;
import kudos.web.beans.form.UserForm;
import kudos.model.Email;
import kudos.model.User;
import kudos.services.EmailService;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.SingleErrorResponse;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Date;

/**
 * Created by chc on 15.8.10.
 */
@Controller
public class RegistrationController extends BaseController {


    Logger LOG = Logger.getLogger(RegistrationController.class);

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Response> register(@ModelAttribute("form") UserForm userForm, Errors errors) throws FormValidationException {
        new UserForm.FormValidator().validate(userForm, errors);
        if(errors.hasErrors()) {
            throw new FormValidationException(errors);
        } else if(!userRepository.exists(userForm.getEmail()) || !userRepository.findOne(userForm.getEmail()).isRegistered()){
            try {
                String userEmail = userForm.getEmail();
                new EmailService().send(new Email(userEmail, new Date().toString(), "confirmationMail",
                        "Welcome to KUDOS app. To verify your email, please paste this link to yout browser: localhost:8080/confirm-email?hashedMail="
                                + userEmail.hashCode()));
                String password = new StrongPasswordEncryptor().encryptPassword(userForm.getPassword());
                User user = new User(password,userEmail,userForm.getName(),userForm.getSurname());
                userRepository.save(user);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch(MessagingException e){
                LOG.info("There was an error with messages");
                return new ResponseEntity<>(new SingleErrorResponse("message.server.error"), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (MongoException e){
                LOG.info("there was an error with mongo");
                return new ResponseEntity<>(new SingleErrorResponse("mongo.server.error"),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            LOG.info("email already exists, fail");
            return new ResponseEntity<>(new SingleErrorResponse("email.already.exists"), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/confirm-email" , method = RequestMethod.GET)
    public ResponseEntity<Response> confirmEmail(@RequestParam String hashedMail){
        String email = new StrongTextEncryptor().decrypt("localhost:8080/confirm-email?hashedMail=b4pX21I30Yy8/jLsQG4v7vVlBpEb7tf8pK9X9S4CGL2RGRLfFHXkSwNz9cg1aWTf");
        User user = userRepository.findOne(email);
        if(user != null){
            user.markUserAsConfirmed();
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SingleErrorResponse("user.not.found"),HttpStatus.NOT_FOUND);
        }
    }


}
