package kudos.web.controllers;

import com.google.common.base.Optional;
import kudos.exceptions.BusinessException;
import kudos.exceptions.KudosExceededException;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.web.beans.form.KudosTransferForm;
import kudos.web.beans.response.*;
import kudos.web.exceptions.FormValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

/**
 * Created by chc on 15.8.11.
 */
@Controller
@RequestMapping("/kudos")
public class KudosController extends BaseController {

    @RequestMapping(value = "/send-kudos", method = RequestMethod.POST)
    public ResponseEntity<Response> sendKudos(@ModelAttribute("form") KudosTransferForm kudosTransferForm, Errors errors, Principal principal) throws FormValidationException, BusinessException {
        new KudosTransferForm.KudosFormValidator().validate(kudosTransferForm, errors);

        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        Optional<User> receiver = usersService.findByEmail(kudosTransferForm.getReceiverEmail());

        if (!receiver.isPresent()) {
            return new ResponseEntity<>(new SingleErrorResponse("receiver.not.exist"), HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = kudosService.giveKudos(receiver.get(),
                Integer.parseInt(kudosTransferForm.getAmount()), kudosTransferForm.getMessage());
        return new ResponseEntity<>(new TransferResponse(transaction), HttpStatus.CREATED);

    }

    @RequestMapping(value = "/get-incoming-transactions", method = RequestMethod.GET)
    public ResponseEntity<Response> showIncomingTransactionHistory(Principal principal) {
        String email = principal.getName();
        List allUserTransactions = transactionRepository.findTransactionsByReceiverEmail(email);
        return new ResponseEntity<>(new TransactionHistoryResponse(allUserTransactions), HttpStatus.OK);

    }

    @RequestMapping(value = "/get-outgoing-transactions", method = RequestMethod.GET)
    public ResponseEntity<Response> showOutcomingTransactionHistory(Principal principal) {
        String email = principal.getName();
        List allUserTransactions = transactionRepository.findTransactionsBySenderEmail(email);
        return new ResponseEntity<>(new TransactionHistoryResponse(allUserTransactions), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-remaining-kudos", method = RequestMethod.GET)
    public ResponseEntity<Response> showRemainingKudos(Principal principal) {
        int amount = kudosService.getFreeKudos(usersService.getLoggedUser().get());
        return new ResponseEntity<>(StatusResponse.showKudosStatus(amount + ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-received-kudos", method = RequestMethod.GET)
    public ResponseEntity<Response> showReceivedKudos(Principal principal) {
        int amount = kudosService.getKudos(usersService.getLoggedUser().get());
        return new ResponseEntity<>(StatusResponse.showKudosStatus(amount+""), HttpStatus.OK);
    }

}
