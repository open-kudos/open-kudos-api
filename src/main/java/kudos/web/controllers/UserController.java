package kudos.web.controllers;

import com.google.common.base.Strings;
import kudos.exceptions.KudosExceededException;
import kudos.web.beans.form.KudosTransferForm;
import kudos.web.beans.form.MyProfileForm;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.web.exceptions.FormValidationException;
import kudos.web.beans.response.*;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;


/**
 * Created by chc on 15.7.23.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private static Logger LOG = Logger.getLogger(UserController.class.getName());


    @RequestMapping(value = "/delete-me", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteMyAccount(HttpSession session, Principal principal) {
        String userEmail = principal.getName();
        User user = userRepository.findOne(userEmail);
        user.setIsRegistered(false);
        userRepository.save(user);
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ResponseEntity<Response> showHomePage(Principal principal) {
        User user = userRepository.findOne(principal.getName());
        if (user != null && !user.isCompleted()) {
            return new ResponseEntity<>(new SingleErrorResponse("profile.not.completed"), HttpStatus.BAD_REQUEST);
        } else if (user != null) {
            return new ResponseEntity<>(UserResponse.showUser(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SingleErrorResponse("user.not.exist"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "complete-profile", method = RequestMethod.POST)
    public ResponseEntity<Response> completeUserProfile(@ModelAttribute("form") MyProfileForm myProfileForm, Errors errors, Principal principal) throws FormValidationException {
        new MyProfileForm.MyProfileValidator().validate(myProfileForm, errors);

        if(errors.hasErrors()){
            throw new FormValidationException(errors);
        } else {
            User user = userRepository.findOne(principal.getName());

            String email = user.getEmail();
            String password = user.getPassword();
            String name = user.getFirstName();
            String surname = user.getLastName();

            String newEmail = myProfileForm.getEmail();
            String newPassword = myProfileForm.getNewPassword();
            String newFirstName = myProfileForm.getFirstName();
            String newLastName = myProfileForm.getLastName();

            if (!Strings.isNullOrEmpty(newEmail) && !newEmail.equals(email)) {
                email = newEmail;
            }

            if (!Strings.isNullOrEmpty(newPassword) && !new StrongPasswordEncryptor().checkPassword(newPassword, password)) {
                password = new StrongPasswordEncryptor().encryptPassword(newPassword);
            }

            if (!Strings.isNullOrEmpty(newFirstName) && !name.equals(newFirstName)) {
                name = newFirstName;
            }

            if (!Strings.isNullOrEmpty(newLastName) && !surname.equals(newLastName)) {
                surname = newLastName;
            }

            String birthday = myProfileForm.getBirthday();
            String phone = myProfileForm.getPhone();
            String startedToWork = myProfileForm.getStartedToWorkDate();
            String position = myProfileForm.getPosition();
            String departament = myProfileForm.getDepartment();
            String location = myProfileForm.getLocation();
            String team = myProfileForm.getTeam();
            boolean showBirthday = myProfileForm.getShowBirthday();

            user.updateUserWithAdditionalInformation(password, email, name, surname, birthday, phone, startedToWork, position, departament,
                    location, team, true, showBirthday);
            userRepository.save(user);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
