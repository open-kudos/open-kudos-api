package kudos.web.beans.response.userActionResponse;

import kudos.model.Action;
import kudos.web.beans.response.KudosTransactionResponse;

public class UserTransactionActionResponse extends UserAction {

    KudosTransactionResponse transactionResponse;

    public UserTransactionActionResponse(Action action) {
        super(action);
        this.transactionResponse = new KudosTransactionResponse(action.getTransaction(), action.getTransaction().getType().toString());
    }

    public KudosTransactionResponse getTransactionResponse() {
        return transactionResponse;
    }

    public void setTransactionResponse(KudosTransactionResponse transactionResponse) {
        this.transactionResponse = transactionResponse;
    }
}
