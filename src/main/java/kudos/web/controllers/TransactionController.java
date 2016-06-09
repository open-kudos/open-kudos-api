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
@Api(name = "Transactions Controller", description = "Controller for kudos transactions")
@Controller
@RequestMapping("/transaction")
public class TransactionController extends BaseController {

    @ApiMethod(description = "Service to watch if kudos feed list has changed")
    @RequestMapping(value = "/kudosFeedPool", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    boolean kudosTransferChanged(String lastTransactionTimestamp) throws UserException {
        return transactionService.isLastTransactionChanged(lastTransactionTimestamp);
    }

    @ApiMethod(description = "Service to get paged transactions by status." + "<p> status=status&page=0&pageSize=1 <p>")
    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    List<Transaction> listOfTransactionsByStatus(Transaction.Status status, int page, int pageSize) throws UserException {
        return transactionService.getPageableTransactionsByStatus(status, page, pageSize);
    }

    @ApiMethod(description = "Service to get new transactions like notifications")
    @RequestMapping(value = "/newTransactions", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    List<Transaction> listOfNewTransactions() throws UserException {
        return transactionService.getNewTransactions(usersService.getLoggedUser().get().getLastSeenTransactionTimestamp());
    }

    @ApiMethod(description = "Service to set new transactions timestamp for notifications")
    @RequestMapping(value = "/timestamp", method = RequestMethod.POST)
    public
    @ApiResponseObject
    @ResponseBody
    void setLastSeenTransaction(String timestamp) throws UserException {
         transactionService.setLastSeenTransactionTimestamp(timestamp);
    }

    @ApiMethod(description = "Service to set new transactions timestamp for notifications")
    @RequestMapping(value = "/testTime", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    String getLastSeenTransaction() throws UserException {
        return usersService.getLoggedUser().get().getLastSeenTransactionTimestamp();
    }



}
