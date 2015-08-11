package kudos.web.beans.response;

import kudos.model.Transaction;

/**
 * Created by chc on 15.8.5.
 */
public class TransferResponse extends Response {

    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransferResponse(Transaction transaction) {
        this.transaction = transaction;
    }
}
