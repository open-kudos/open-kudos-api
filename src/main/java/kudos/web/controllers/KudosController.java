package kudos.web.controllers;

import com.google.common.base.Optional;
import kudos.exceptions.BusinessException;
import kudos.exceptions.KudosExceededException;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.web.beans.form.KudosTransferForm;
import kudos.web.beans.response.*;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.*;
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
@Api(name = "kudos controller", description = "Controller for giving/pending/receiving kudos")
@Controller
@RequestMapping("/kudos")
public class KudosController extends BaseController {

    @ApiMethod(description = "Service to send kudos")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "receiverEmail"),
            @ApiQueryParam(name = "message", description = "Message, explaining why user is giving kudos")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "receiver.email.not.specified",
                    description = "If receiver email was not specified"),
            @ApiError(code = "receiver.email.incorrect",
                    description = "If receiver email was incorrect"),
            @ApiError(code = "amount.negative.or.zero",
                    description = "If specified amount was negative or equal to zero"),
            @ApiError(code = "amount.not.digit",
                    description = "If specified amount is not a digit"),
            @ApiError(code = "receiver.not.exist",
                    description = "If kudos receiver does not exist")
    })
    @RequestMapping(value = "/send-kudos", method = RequestMethod.POST)
    public ResponseEntity<Response> sendKudos(@ModelAttribute("form") KudosTransferForm kudosTransferForm, Errors errors) throws FormValidationException, BusinessException, UserException {
        new KudosTransferForm.KudosFormValidator().validate(kudosTransferForm, errors);

        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }

        Optional<User> user = usersService.findByEmail(kudosTransferForm.getReceiverEmail());

        if(user.isPresent()) {
            Transaction transaction = kudosService.giveKudos(
                    user.get(), Integer.parseInt(kudosTransferForm.getAmount()), kudosTransferForm.getMessage());
            return new ResponseEntity<>(new TransferResponse(transaction), HttpStatus.CREATED);
        } else {
            throw new UserException("receiver.not.exist");
        }

    }

    @ApiMethod(description = "Service to get all incoming kudos transactions")
    @RequestMapping(value = "/incoming-transactions", method = RequestMethod.GET)
    public ResponseEntity<Response> showIncomingTransactionHistory(Principal principal) {
        String email = principal.getName();
        List allUserTransactions = transactionRepository.findTransactionsByReceiverEmail(email);
        return new ResponseEntity<>(new TransactionHistoryResponse(allUserTransactions), HttpStatus.OK);

    }

    @ApiMethod(description = "Service to get all outgoing kudos transactions")
    @RequestMapping(value = "/outgoing-transactions", method = RequestMethod.GET)
    public ResponseEntity<Response> showOutcomingTransactionHistory(Principal principal) {
        String email = principal.getName();
        List allUserTransactions = transactionRepository.findTransactionsBySenderEmail(email);
        return new ResponseEntity<>(new TransactionHistoryResponse(allUserTransactions), HttpStatus.OK);
    }

    @ApiMethod(description = "Service to get remaining kudos amount")
    @RequestMapping(value = "/get-remaining-kudos", method = RequestMethod.GET)
    public ResponseEntity<Response> showRemainingKudos(Principal principal) throws UserException {
        int amount = kudosService.getFreeKudos(usersService.getLoggedUser().get());
        return new ResponseEntity<>(StatusResponse.showKudosStatus(amount + ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-received-kudos", method = RequestMethod.GET)
    public ResponseEntity<Response> showReceivedKudos(Principal principal) throws UserException {
        int amount = kudosService.getKudos(usersService.getLoggedUser().get());
        return new ResponseEntity<>(StatusResponse.showKudosStatus(amount+""), HttpStatus.OK);
    }

}
