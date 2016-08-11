package kudos.web.beans.response.userActionResponse;

import kudos.model.Action;
import kudos.web.beans.response.IdeaResponse;

/**
 * Created by vytautassugintas on 09/08/16.
 */
public class UserIdeaActionResponse extends UserAction {

    IdeaResponse ideaResponse;

    public UserIdeaActionResponse(Action action) {
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
