package kudos.web.controllers;

import kudos.model.Transaction;
import kudos.web.beans.response.IndexResponse;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.KudosTransactionResponse;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Api(name = "Home Controller",description = "Service to test connection / check whether someone is logged in or not.")
@RestController
public class HomeController extends BaseController {

    @ApiMethod(description = "method to check whether user is logged in or not")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody Response index(Principal principal) {
        return new IndexResponse(principal != null);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public List<KudosTransactionResponse> latestTransactions() {
        return convert(transactionService.getLatestTransactions());
    }

    private List<KudosTransactionResponse> convert(List<Transaction> input) {
        List<KudosTransactionResponse> transactions = new ArrayList<>();
        for(Transaction transaction : input) {
            transactions.add(new KudosTransactionResponse(transaction, transaction.getStatus().toValue()));
        }
        return transactions;
    }
}