package kudos.web.controllers;

import kudos.exceptions.FormValidationException;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.web.beans.request.GiveKudosForm;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.Optional;


@RestController
@RequestMapping("/kudos")
public class KudosController extends BaseController {

    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public void giveKudos(@RequestBody GiveKudosForm form) throws UserException, InvalidKudosAmountException, MessagingException {

        User sender = authenticationService.getLoggedInUser();

        Optional<User> receiver = usersService.findByEmail(form.getReceiverEmail().toLowerCase());

        if(receiver.isPresent()) {
            kudosService.giveKudos(sender, receiver.get(), form.getAmount(), form.getMessage());
        } else {
            String email = sender.getFirstName() + " " + sender.getLastName() + "wanted to give you KUDOS," +
                    " but you are not registered. Maybe it is time to do it? Go to www.openkudos.com and try it!";
            emailService.sendEmail(form.getReceiverEmail().toLowerCase(), email, "Open Kudos");
            throw new UserException("receiver_does_not_exist");
        }

    }
//
//    @ApiMethod(description = "Service to get all incoming kudos transactions")
//    @RequestMapping(value = "/incoming", method = RequestMethod.GET)
//    public @ApiResponseObject @ResponseBody List<Transaction> showIncomingTransactionHistory() throws UserException {
//        return kudosService.getAllLoggedUserIncomingTransactions();
//    }
//
//    @ApiMethod(description = "Service to get all outgoing kudos transactions")
//    @RequestMapping(value = "/outgoing", method = RequestMethod.GET)
//    public @ApiResponseObject @ResponseBody List<Transaction> showOutcomingTransactionHistory() throws UserException {
//        return kudosService.getAllLoggedUserOutgoingTransactions();
//    }
//
//    @ApiMethod(description = "Service to get remaining kudos amount")
//    @RequestMapping(value = "/remaining", method = RequestMethod.GET)
//    public @ApiResponseObject @ResponseBody int showRemainingKudos(Principal principal) throws UserException {
//        return kudosService.getFreeKudos(usersService.getLoggedUser().get());
//    }
//
//    @ApiMethod(description = "Service to get received kudos")
//    @RequestMapping(value = "/received", method = RequestMethod.GET)
//    public @ApiResponseObject @ResponseBody int receivedKudos(Principal principal) throws UserException {
//        return kudosService.getKudos(usersService.getLoggedUser().get());
//    }
}
