package kudos.web.controllers;

import kudos.model.Transaction;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by vytautassugintas on 25/04/16.
 */
@Api(name = "Kudos transactions controller", description = "Controller for kudos transactions")
@Controller
@RequestMapping("/feed")
public class TransactionController extends BaseController {

    @ApiMethod(description = "Service to watch if kudos feed list has changed")
    @RequestMapping(value = "/kudosFeedPool", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    boolean kudosTransferChanged(String lastTransactionTimestamp) throws UserException {
        return lastTransactionTimestamp != null && transactionService.isLastTransactionChanged(lastTransactionTimestamp);
    }

    @ApiMethod(description = "Service to get kudos transactions feed, can be paged")
    @RequestMapping(value = "/feedPaged", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    List<Transaction> showKudosTransactionsFeed(int page, int pageSize) throws UserException {
        return transactionService.getPageableTransactionsMadeThisWeek(page, pageSize);
    }

    /*
    @ApiMethod(description = "Service to get kudos transactions which is completed")
    @RequestMapping(value = "/completed", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    List<Transaction> listOfCompletedTransactions() throws UserException {
        return transactionService.getCompletedTransactions();
    }

    @ApiMethod(description = "Service to get kudos transactions which status is pending")
    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    List<Transaction> listOfPendingTransactions() throws UserException {
        return transactionService.getPendingTransactions();
    }

    @ApiMethod(description = "Service to get kudos transactions which is made from challenges")
    @RequestMapping(value = "/challenge", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    List<Transaction> listOfTransactionsFromChallenge() throws UserException {
        return transactionService.getTransactionsFromChallenge();
    }
    */


    //COMPLETED, FROM_CHALLENGE, PENDING_CHALLENGE

}
