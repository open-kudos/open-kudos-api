package kudos.web.beans.response.followedUsersFeedResponse;

import kudos.model.Action;
import kudos.model.ActionType;

public abstract class FollowedUsersFeed {

    public String userFullName;
    public String userId;
    public ActionType type;
    public String timestamp;

    public FollowedUsersFeed(Action action){
        this.userFullName = action.getUser().getFirstName() + " " + action.getUser().getLastName();
        this.userId = action.getUser().getId();
        this.type = action.getType();
        this.timestamp = action.getTimestamp();
    }
}
