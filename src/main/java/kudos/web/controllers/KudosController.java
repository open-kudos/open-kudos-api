package kudos.web.controllers;

import kudos.exceptions.FormValidationException;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.ActionType;
import kudos.model.Transaction;
import kudos.model.TransactionType;
import kudos.model.User;
import kudos.web.beans.request.GiveKudosForm;
import kudos.web.beans.request.validator.GiveKudosFormValidator;
import kudos.web.beans.response.KudosTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kudos")
public class KudosController extends BaseController {

    @Autowired
    GiveKudosFormValidator giveKudosFormValidator;

    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public KudosTransactionResponse giveKudos(@RequestBody GiveKudosForm form, BindingResult errors) throws UserException,
            InvalidKudosAmountException, MessagingException, FormValidationException {
        giveKudosFormValidator.validate(form, errors);
        if(errors.hasErrors())
            throw new FormValidationException(errors);

        User sender = authenticationService.getLoggedInUser();
        Optional<User> receiver = usersService.findByEmail(form.getReceiverEmail().toLowerCase());

        if(receiver.isPresent()) {
            Transaction transaction = kudosService.giveKudos(sender, receiver.get(), form.getAmount(),form.getMessage());
            actionsService.save(sender, transaction, ActionType.KUDOS_GIVEN);
            return new KudosTransactionResponse(transaction, "GIVEN");
        } else {
//            String email = sender.getFirstName() + " " + sender.getLastName() + "wanted to give you KUDOS," +
//                    " but you are not registered. Maybe it is time to do it? Go to www.openkudos.com and try it!";
//            emailService.sendEmail(form.getReceiverEmail().toLowerCase(), email, "Open Kudos");
            throw new UserException("receiver_does_not_exist");
        }
    }

    @RequestMapping(value = "/history/given", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getGivenKudosHistory(@RequestParam(value="page") int page,
                                                               @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getGivenKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/received", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getReceivedKudosHistory(@RequestParam(value="page") int page,
                                                                  @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getReceivedKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getKudosHistory(@RequestParam(value="page") int page,
                                                          @RequestParam(value="size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/{userId}", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getKudosHistory(@PathVariable String userId,
                                                          @RequestParam(value="page") int page,
                                                          @RequestParam(value="size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(transactionService.getKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/received/{userId}", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getReceivedKudosHistory(@PathVariable String userId,
                                                                  @RequestParam(value="page") int page,
                                                                  @RequestParam(value="size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(transactionService.getReceivedKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/given/{userId}", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getGivenKudosHistory(@PathVariable String userId,
                                                               @RequestParam(value="page") int page,
                                                               @RequestParam(value="size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(transactionService.getGivenKudosHistory(user, new PageRequest(page, size)), user);
    }

    private Page<KudosTransactionResponse> convert(Page<Transaction> input, User user) {
        List<KudosTransactionResponse> transactions = new ArrayList<>();
        for(Transaction transaction : input.getContent()) {
            transactions.add(new KudosTransactionResponse(transaction, getKudosTransactionType(user, transaction)));
        }
        return new PageImpl<>(transactions, new PageRequest(input.getNumber(), input.getSize()), input.getTotalElements());
    }

    private String getKudosTransactionType(User user, Transaction transaction) {
        if(transaction.getType() == TransactionType.KUDOS && transaction.getSender().getId().equals(user.getId())) {
            return "GIVEN";
        } else if(transaction.getType() == TransactionType.KUDOS && transaction.getReceiver().getId().equals(user.getId())) {
            return "RECEIVED";
        } else {
            return "UNKNOWN";
        }
    }

}
