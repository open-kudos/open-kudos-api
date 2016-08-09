package kudos.web.beans.response.followedUsersFeedResponse;

import kudos.model.Action;
import kudos.web.beans.response.KudosTransactionResponse;

public class FollowedUserTransactionResponse extends FollowedUsersFeed {

    KudosTransactionResponse transactionResponse;

    public FollowedUserTransactionResponse(Action action) {
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
