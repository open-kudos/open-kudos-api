package kudos.web.controller;

import com.google.common.base.Strings;
import kudos.dao.repositories.TransactionRepository;
import kudos.model.*;
import kudos.services.control.KudosAmountControlService;
import kudos.services.transfers.KudosTransferService;
import kudos.web.model.*;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
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

    KudosTransferService kudosTransferService;
    KudosAmountControlService kudosAmountControlService;

    @Autowired
    public UserController(KudosTransferService kudosTransferService, KudosAmountControlService kudosAmountControlService){
        this.kudosTransferService =  kudosTransferService;
        this.kudosAmountControlService = kudosAmountControlService;
    }

    @RequestMapping(value = "/delete-me", method = RequestMethod.POST)
    public Response deleteMyAccount(HttpSession session,Principal principal){
        String userEmail = principal.getName();
        User user = userRepository.findOne(userEmail);
        user.setIsRegistered(false);
        userRepository.save(user);
        session.invalidate();
        return DataResponse.success();
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ResponseEntity<Response> showHomePage(Principal principal){
        User user = userRepository.findOne(principal.getName());
        if(user != null && !user.isCompleted()){
            return new ResponseEntity<>(DataResponse.fail("You must complete your profile"),HttpStatus.BAD_REQUEST);
        }
        else if(user != null){
            return new ResponseEntity<>(UserResponse.showUser(user),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DataResponse("fail","user does not exist",null,null),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "complete-profile", method = RequestMethod.POST)
    public ResponseEntity<Response> completeUserProfile(@ModelAttribute("form") MyProfileForm myProfileForm, Errors errors,Principal principal){
        new MyProfileForm.MyProfileValidator().validate(myProfileForm,errors);

        if(!errors.hasErrors()){
            User user = userRepository.findOne(principal.getName());

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

            user.updateUserWithAdditionalInformation(password, email, name, surname, birthday, phone, startedToWork, position, departament,
                    location, team, true, showBirthday);
            userRepository.save(user);

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

           final Transaction transaction;

           switch(kudosType){
               case "MINIMUM":
                   transaction = new Transaction(collegueEmail, myEmail, Transaction.KudosType.MINIMUM, message);
               break;
               case "NORMAL":
                   transaction = new Transaction(collegueEmail,myEmail, Transaction.KudosType.NORMAL, message);
               break;
               case "MAXIMUM":
                   transaction = new Transaction(collegueEmail,myEmail, Transaction.KudosType.MAXIMUM, message);
               break;
               default: transaction = new Transaction(collegueEmail,myEmail, Transaction.KudosType.NORMAL, message);
           }
           return kudosTransferService.transferKudos(transaction);

       } else {
           return new ResponseEntity<>(ErrorResponse.create(errors.getFieldErrors()),HttpStatus.BAD_REQUEST);
       }

    }

    @RequestMapping(value = "/show-incoming-transactions", method = RequestMethod.GET)
    public ResponseEntity<Response> showIncomingTransactionHistory(Principal principal){
        String email = principal.getName();
        List allUserTransactions = transactionRepository.findTransactionsByReceiverEmail(email);

        if(allUserTransactions.size() == 0){
            return new ResponseEntity<>(TransactionHistoryResponse.failedToShow("Currently you have not any incoming transactions"),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new TransactionHistoryResponse(allUserTransactions),HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/show-outgoing-transactions", method = RequestMethod.GET)
    public ResponseEntity<Response> showOutcomingTransactionHistory(Principal principal){
        String email = principal.getName();
        List allUserTransactions = transactionRepository.findTransactionsBySenderEmail(email);

        if(allUserTransactions.size() == 0){
            return new ResponseEntity<>(TransactionHistoryResponse.failedToShow("Currently you have not any incoming transactions"),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new TransactionHistoryResponse(allUserTransactions),HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/show-remaining-kudos", method = RequestMethod.GET)
    public ResponseEntity<Response> showRemainingKudos(Principal principal){
        int amount = kudosAmountControlService.howManyKudosUserCanSpend(principal.getName());
        return new ResponseEntity<>(StatusResponse.showKudosStatus(amount+""),HttpStatus.OK);
    }

    @RequestMapping(value = "/challenge-friend", method = RequestMethod.POST)
    public ResponseEntity<Response> challengeFriend(@ModelAttribute("form")ChallengeTransferForm challengeTransferForm)

}
