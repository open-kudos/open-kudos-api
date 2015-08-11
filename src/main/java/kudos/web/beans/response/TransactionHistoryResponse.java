package kudos.web.beans.response;

import java.util.List;

/**
 * Created by chc on 15.8.6.
 */
public class TransactionHistoryResponse extends Response {

    List transactions;

    public TransactionHistoryResponse(List transactions){
        this.transactions = transactions;
    }

    public List getTransactions() {
        return transactions;
    }

    public void setTransactions(List transactions) {
        this.transactions = transactions;
    }


}
