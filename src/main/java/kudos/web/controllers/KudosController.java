package kudos.web.controllers;

import kudos.exceptions.KudosExceededException;
import kudos.model.Transaction;
import kudos.web.beans.form.KudosTransferForm;
import kudos.web.beans.response.*;
import kudos.web.exceptions.FormValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

/**
 * Created by chc on 15.8.11.
 */
public class KudosController extends BaseController {

    @RequestMapping(value = "/send-kudos", method = RequestMethod.POST)
    public ResponseEntity<Response> sendKudos(@ModelAttribute("form") KudosTransferForm kudosTransferForm, Errors errors, Principal principal) throws FormValidationException {
        new KudosTransferForm.KudosFormValidator().validate(kudosTransferForm,errors);

        if(errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        try {
            Transaction transaction = kudosService.transfer(kudosTransferForm.getReceiverEmail(),
                    Integer.parseInt(kudosTransferForm.getAmount()), kudosTransferForm.getMessage());
            return new ResponseEntity<>(new TransferResponse(transaction), HttpStatus.CREATED);
        } catch(KudosExceededException e) {
            return new ResponseEntity<>(new SingleErrorResponse("kudos.exceeded"), HttpStatus.BAD_REQUEST);
        }


    }

    @RequestMapping(value = "/show-incoming-transactions", method = RequestMethod.GET)
    public ResponseEntity<Response> showIncomingTransactionHistory(Principal principal) {
        String email = principal.getName();
        List allUserTransactions = transactionRepository.findTransactionsByReceiverEmail(email);
        return new ResponseEntity<>(new TransactionHistoryResponse(allUserTransactions), HttpStatus.OK);

    }

    @RequestMapping(value = "/show-outgoing-transactions", method = RequestMethod.GET)
    public ResponseEntity<Response> showOutcomingTransactionHistory(Principal principal) {
        String email = principal.getName();
        List allUserTransactions = transactionRepository.findTransactionsBySenderEmail(email);
        return new ResponseEntity<>(new TransactionHistoryResponse(allUserTransactions), HttpStatus.OK);
    }

    @RequestMapping(value = "/show-remaining-kudos", method = RequestMethod.GET)
    public ResponseEntity<Response> showRemainingKudos(Principal principal) {
        int amount = kudosService.periodBalance(principal.getName(), kudosBusinessStrategy.getStartTime());
        return new ResponseEntity<>(StatusResponse.showKudosStatus(amount + ""), HttpStatus.OK);
    }

}
