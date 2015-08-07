package kudos.web.model.specificResponse;

import kudos.web.model.mainResponse.Response;

import java.util.List;

/**
 * Created by chc on 15.8.6.
 */
public class TransactionHistoryResponse extends Response {

    List transactions;
    String errorMessage;

    public TransactionHistoryResponse(List transactions){
        this.transactions = transactions;
    }

    public TransactionHistoryResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public static Response failedToShow(String errorMessage){
        return new TransactionHistoryResponse(errorMessage);
    }

    public List getTransactions() {
        return transactions;
    }

    public void setTransactions(List transactions) {
        this.transactions = transactions;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
