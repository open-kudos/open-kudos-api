package kudos.services.util;

import kudos.exceptions.UserException;
import kudos.model.Action;
import kudos.web.beans.response.userActionResponse.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class ActionConverter {

    public Page<UserAction> convertActionsPage(Page<Action> actions) throws UserException {
        List<UserAction> response = new ArrayList<>();

        for(Action item : actions.getContent()) {
            response.add(getActionResponseObject(item));
        }

        return new PageImpl<>(response, new PageRequest(actions.getNumber(), actions.getSize()),
                actions.getTotalElements());
    }

    private UserAction getActionResponseObject(Action action) {
        switch (action.getType()) {
            case KUDOS_GIVEN:
                return new UserTransactionActionResponse(action);
            case COMMENTED:
                return new UserCommentActionResponse(action);
            case STARTED_TO_FOLLOW:
                return new UserRelationActionResponse(action);
            case ADDED_NEW_IDEA:
                return new UserIdeaActionResponse(action);
            case PURCHASED_SHOP_ITEM:
                return new UserShopActionResponse(action);
            default:
                return new UserChallengeActionResponse(action);
        }
    }

}
