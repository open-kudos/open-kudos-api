package kudos.web.controller;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import kudos.dao.UserDAO;
import kudos.dao.UserInMemoryDAO;
import kudos.dao.repositories.UserRepository;
import kudos.model.*;
import kudos.services.transfers.TransferKudos;
import kudos.web.model.*;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;


/**
 * Created by chc on 15.7.23.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserDAO dao;

    private static Logger LOG = Logger.getLogger(UserController.class.getName());

    @RequestMapping(value = "/delete-me", method = RequestMethod.POST)
    public Response deleteMyAccount(HttpSession session,Principal principal){
        userDAO.remove(principal.getName());
        session.invalidate();
        return DataResponse.success();
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ResponseEntity<Response> showHomePage(Principal principal){
        Optional<User> user = userDAO.getUserByEmail(principal.getName());
        if(user.isPresent() && !user.get().isCompleted()){
            return new ResponseEntity<>(DataResponse.fail("You must complete your profile"),HttpStatus.BAD_REQUEST);
        }
        else if(user.isPresent()){
            return new ResponseEntity<>(UserResponse.showUser(user.get()),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DataResponse("fail","user does not exist",null,null),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "complete-profile", method = RequestMethod.POST)
    public ResponseEntity<Response> completeUserProfile(@ModelAttribute("form") MyProfileForm myProfileForm, Errors errors,Principal principal){
        new MyProfileForm.MyProfileValidator().validate(myProfileForm,errors);

        if(!errors.hasErrors()){
            User user = userDAO.getUserByEmail(principal.getName()).get();

            String email = user.getEmail();
            String password = user.getPassword();
            String name = user.getFirstName();
            String surname = user.getLastName();

            String newEmail = myProfileForm.getEmail();
            String newPassword = myProfileForm.getNewPassword();
            String newFirstName = myProfileForm.getFirstName();
            String newLastName = myProfileForm.getLastName();

            if(!Strings.isNullOrEmpty(newEmail) && !newEmail.equals(email)){
                email = newEmail;
            }

            if(!Strings.isNullOrEmpty(newPassword) && !new StrongPasswordEncryptor().checkPassword(newPassword, password)){
                 password = new StrongPasswordEncryptor().encryptPassword(newPassword);
            }

            if(!Strings.isNullOrEmpty(newFirstName) && !name.equals(newFirstName)){
                name = newFirstName;
            }

            if(!Strings.isNullOrEmpty(newLastName) && !surname.equals(newLastName)){
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

            user.updateUserWithAdditionalInformation(password, email, name, surname, birthday, phone, startedToWork, position, departament, location, team, true, showBirthday);
            userDAO.update(user);

            return new ResponseEntity<>(DataResponse.success(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(ErrorResponse.create(errors.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/send-kudos", method = RequestMethod.POST)
    public ResponseEntity<Response> sendKudos(@ModelAttribute("form")KudosTransferForm kudosTransferForm, Errors errors, Principal principal){
       new KudosTransferForm.KudosFormValidator().validate(kudosTransferForm,errors);
       String myEmail = principal.getName();
       String collegueEmail = kudosTransferForm.getReceiverEmail();
       String kudosType = kudosTransferForm.getType().toUpperCase();
       String message =  kudosTransferForm.getMessage();
       if(!errors.hasErrors()){

           final Kudos kudos;

           switch(kudosType){
               case "MINIMUM":
                   kudos = new Kudos(collegueEmail, Kudos.KudosType.MINIMUM, message);
               break;
               case "NORMAL":
                   kudos = new Kudos(collegueEmail, Kudos.KudosType.NORMAL, message);
               break;
               case "MAXIMUM":
                   kudos = new Kudos(collegueEmail, Kudos.KudosType.MAXIMUM, message);
               break;
               default: kudos = new Kudos(collegueEmail, Kudos.KudosType.NORMAL, message);
           }

           new TransferKudos().transferKudos(myEmail,collegueEmail,kudos);
           return new ResponseEntity<>(DataResponse.success("Kudos transaction completed"),HttpStatus.OK);

       } else {
           return new ResponseEntity<>(ErrorResponse.create(errors.getFieldErrors()),HttpStatus.BAD_REQUEST);
       }

    }

}
