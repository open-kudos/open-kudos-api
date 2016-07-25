package kudos.web.controllers;

import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.web.beans.request.GiveKudosForm;
import kudos.web.beans.response.KudosTransactionResponse;
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
    public KudosTransactionResponse giveKudos(@RequestBody GiveKudosForm form) throws UserException,
            InvalidKudosAmountException, MessagingException {

        User sender = authenticationService.getLoggedInUser();
        Optional<User> receiver = usersService.findByEmail(form.getReceiverEmail().toLowerCase());

        if(receiver.isPresent()) {
            return new KudosTransactionResponse(kudosService.giveKudos(sender, receiver.get(), form.getAmount(),
                    form.getMessage()));
        } else {
            String email = sender.getFirstName() + " " + sender.getLastName() + "wanted to give you KUDOS," +
                    " but you are not registered. Maybe it is time to do it? Go to www.openkudos.com and try it!";
            emailService.sendEmail(form.getReceiverEmail().toLowerCase(), email, "Open Kudos");
            throw new UserException("receiver_does_not_exist");
        }
    }

}
