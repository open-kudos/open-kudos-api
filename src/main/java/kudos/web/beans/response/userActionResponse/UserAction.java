package kudos.web.beans.response.userActionResponse;

import kudos.model.Action;
import kudos.model.status.ActionType;

public abstract class UserAction {

    public String userFullName;
    public String userId;
    public ActionType type;
    public String timestamp;

    public UserAction(Action action){
        this.userFullName = action.getUser().getFirstName() + " " + action.getUser().getLastName();
        this.userId = action.getUser().getId();
        this.type = action.getType();
        this.timestamp = action.getTimestamp();
    }
}
