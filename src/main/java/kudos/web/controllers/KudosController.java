package kudos.web.controllers;

import kudos.exceptions.BusinessException;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.web.beans.form.KudosTransferForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
            @ApiQueryParam(name = "receiverEmail", description = "For testing use testK@google.lt"),
            @ApiQueryParam(name = "message", description = "Message, explaining why user is giving kudos")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "receiver_email_not_specified",
                    description = "If receiver email was not specified"),
            @ApiError(code = "receiver_email_incorrect",
                    description = "If receiver email was incorrect"),
            @ApiError(code = "amount_negative_or_zero",
                    description = "If specified amount was negative or equal to zero"),
            @ApiError(code = "amount_not_digit",
                    description = "If specified amount is not a digit"),
            @ApiError(code = "receiver_not_exist",
                    description = "If kudos receiver does not exist")
    })
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public @ApiResponseObject @ResponseBody Transaction sendKudos(KudosTransferForm kudosTransferForm, Errors errors)
            throws FormValidationException, BusinessException, UserException {

        new KudosTransferForm.KudosFormValidator().validate(kudosTransferForm, errors);

        if (usersService.getLoggedUser().get().getEmail().equals(kudosTransferForm.getReceiverEmail())){
            throw new UserException("cant.send.kudos.to.yourself");
        }

        if (errors.hasErrors())
            throw new FormValidationException(errors);

        User user = usersService.findByEmail(kudosTransferForm.getReceiverEmail())
                .orElseThrow(() -> new UserException("receiver.not.exist"));

        return kudosService.giveKudos(user, Integer.parseInt(kudosTransferForm.getAmount()), kudosTransferForm.getMessage());
    }

    @ApiMethod(description = "Service to get all incoming kudos transactions")
    @RequestMapping(value = "/incoming", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Transaction> showIncomingTransactionHistory() throws UserException {
        return kudosService.getAllLoggedUserIncomingTransactions();
    }

    @ApiMethod(description = "Service to get all outgoing kudos transactions")
    @RequestMapping(value = "/outgoing", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<Transaction> showOutcomingTransactionHistory() throws UserException {
        return kudosService.getAllLoggedUserOutgoingTransactions();
    }

    @ApiMethod(description = "Service to get remaining kudos amount")
    @RequestMapping(value = "/remaining", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody int showRemainingKudos(Principal principal) throws UserException {
        return kudosService.getFreeKudos(usersService.getLoggedUser().get());
    }

    @ApiMethod(description = "Service to get received kudos")
    @RequestMapping(value = "/received", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody int receivedKudos(Principal principal) throws UserException {
        return kudosService.getKudos(usersService.getLoggedUser().get());
    }


}
