package kudos.web.beans.response.followedUsersFeedResponse;

import kudos.model.Action;
import kudos.web.beans.response.IdeaResponse;

/**
 * Created by vytautassugintas on 09/08/16.
 */
public class FollowedUserIdeaResponse extends FollowedUsersFeed {

    IdeaResponse ideaResponse;

    public FollowedUserIdeaResponse(Action action) {
        super(action);
        this.ideaResponse = new IdeaResponse(action.getIdea());
    }

    public IdeaResponse getIdeaResponse() {
        return ideaResponse;
    }

    public void setIdeaResponse(IdeaResponse ideaResponse) {
        this.ideaResponse = ideaResponse;
    }
}
