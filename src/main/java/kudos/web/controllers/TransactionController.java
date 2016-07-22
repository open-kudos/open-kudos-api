package kudos.web.controllers;

import org.jsondoc.core.annotation.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(name = "Transactions Controller", description = "Controller for kudos transactions")
@Controller
@RequestMapping("/transaction")
public class TransactionController extends BaseController {
//
//    @ApiMethod(description = "Service to watch if kudos feed list has changed")
//    @RequestMapping(value = "/kudosFeedPool", method = RequestMethod.GET)
//    public
//    @ApiResponseObject
//    @ResponseBody
//    boolean kudosTransferChanged(String lastTransactionTimestamp) throws UserException {
//        return transactionService.isLastTransactionChanged(lastTransactionTimestamp);
//    }
//
//    @ApiMethod(description = "Service to get paged transactions by status." + "<p> status=status&page=0&pageSize=1 <p>")
//    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
//    public
//    @ApiResponseObject
//    @ResponseBody
//    List<TransactionResponse> listOfTransactionsByStatus(TransactionType transactionType, int page, int pageSize) throws UserException {
//        return transactionService.getPageableTransactionsByStatus(transactionType, page, pageSize);
//    }
//
//    @ApiMethod(description = "Service to get new transactions like notifications")
//    @RequestMapping(value = "/newTransactions", method = RequestMethod.GET)
//    public
//    @ApiResponseObject
//    @ResponseBody
//    List<TransactionResponse> listOfNewTransactions() throws UserException {
//        return transactionService.getNewTransactions(usersService.getLoggedUser().get().getLastNotificationCheckTime());
//    }
//
//    @ApiMethod(description = "Service to set new transactions timestamp for notifications")
//    @RequestMapping(value = "/timestamp", method = RequestMethod.POST)
//    public
//    @ApiResponseObject
//    @ResponseBody
//    void setLastSeenTransaction(String timestamp) throws UserException {
//         transactionService.setLastSeenTransactionTimestamp(timestamp);
//    }
//
//    @ApiMethod(description = "Service to set new transactions timestamp for notifications")
//    @RequestMapping(value = "/testTime", method = RequestMethod.GET)
//    public
//    @ApiResponseObject
//    @ResponseBody
//    String getLastSeenTransaction() throws UserException {
//        return usersService.getLoggedUser().get().getLastNotificationCheckTime();
//    }
}
