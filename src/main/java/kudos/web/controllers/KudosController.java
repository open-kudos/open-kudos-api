package kudos.web.controllers;

import kudos.exceptions.FormValidationException;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.status.ActionType;
import kudos.model.Transaction;
import kudos.model.status.TransactionType;
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
import java.util.Map;
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

        if (errors.hasErrors())
            throw new FormValidationException(errors);

        User sender = authenticationService.getLoggedInUser();
        Optional<User> receiver = usersService.findByEmail(form.getReceiverEmail().toLowerCase());

        if (receiver.isPresent()) {
            Transaction transaction = kudosService.giveKudos(sender, receiver.get(), form.getAmount(), form.getMessage(), form.getEndorsement());
            actionsService.save(sender, transaction, ActionType.KUDOS_GIVEN);
            levelUpService.increaseExperience(sender, transaction.getAmount());
            return new KudosTransactionResponse(transaction, "GIVEN");
        } else {
            throw new UserException("receiver_does_not_exist");
        }
    }

    @RequestMapping(value = "/history/given", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getGivenKudosHistory(@RequestParam(value = "page") int page,
                                                               @RequestParam(value = "size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getGivenKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/received", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getReceivedKudosHistory(@RequestParam(value = "page") int page,
                                                                  @RequestParam(value = "size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getReceivedKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getKudosHistory(@RequestParam(value = "page") int page,
                                                          @RequestParam(value = "size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/{userId}", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getUserKudosHistory(@PathVariable String userId,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(transactionService.getKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/received/{userId}", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getUserReceivedKudosHistory(@PathVariable String userId,
                                                                  @RequestParam(value = "page") int page,
                                                                  @RequestParam(value = "size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(transactionService.getReceivedKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/history/given/{userId}", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getUserGivenKudosHistory(@PathVariable String userId,
                                                               @RequestParam(value = "page") int page,
                                                               @RequestParam(value = "size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(transactionService.getGivenKudosHistory(user, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/transactions/by/endorsement/{endorsement}", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getTransactionsByEndorsement(@PathVariable String endorsement,
                                                                       @RequestParam(value = "page") int page,
                                                                       @RequestParam(value = "size") int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return convert(transactionService.getKudosTransactionsByEdorsement(user, endorsement, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/transactions/by/endorsement/{userId}/{endorsement}", method = RequestMethod.GET)
    public Page<KudosTransactionResponse> getUserTransactionsByEndorsement(@PathVariable String userId,
                                                                           @PathVariable String endorsement,
                                                                           @RequestParam(value = "page") int page,
                                                                           @RequestParam(value = "size") int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return convert(transactionService.getKudosTransactionsByEdorsement(user, endorsement, new PageRequest(page, size)), user);
    }

    @RequestMapping(value = "/endorsements", method = RequestMethod.GET)
    public Map<String, Integer> getEndorsements() throws UserException {
        User user = authenticationService.getLoggedInUser();
        return transactionService.getEndorsementsMap(user);
    }

    @RequestMapping(value = "/endorsements/{userId}", method = RequestMethod.GET)
    public Map<String, Integer> getUserEndorsements(@PathVariable String userId) throws UserException {
        User user = usersService.findByUserId(userId);
        return transactionService.getEndorsementsMap(user);
    }

    private Page<KudosTransactionResponse> convert(Page<Transaction> input, User user) {
        List<KudosTransactionResponse> transactions = new ArrayList<>();
        for (Transaction transaction : input.getContent()) {
            String type = getKudosTransactionType(user, transaction);
            if (!type.equals("UNKNOWN"))
                transactions.add(new KudosTransactionResponse(transaction, getKudosTransactionType(user, transaction)));
        }
        return new PageImpl<>(transactions, new PageRequest(input.getNumber(), input.getSize()), input.getTotalElements());
    }

    private String getKudosTransactionType(User user, Transaction transaction) {
        if (transaction.getType() == TransactionType.KUDOS && transaction.getSender().getId().equals(user.getId())) {
            return "GIVEN";
        } else if (transaction.getType() == TransactionType.KUDOS && transaction.getReceiver().getId().equals(user.getId())) {
            return "RECEIVED";
        } else {
            return "UNKNOWN";
        }
    }

}
