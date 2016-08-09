package kudos.web.beans.response.followedUsersFeedResponse;

import kudos.model.Action;
import kudos.web.beans.response.RelationResponse;

public class FollowedUserRelationResponse extends FollowedUsersFeed{

    RelationResponse relationResponse;

    public FollowedUserRelationResponse(Action action){
        super(action);
        this.relationResponse = new RelationResponse(action.getRelation());
    }

    public RelationResponse getRelationResponse() {
        return relationResponse;
    }

    public void setRelationResponse(RelationResponse relationResponse) {
        this.relationResponse = relationResponse;
    }
}
