package kudos.web.beans.response.userActionResponse;

import kudos.model.Action;
import kudos.web.beans.response.RelationResponse;

public class UserRelationActionResponse extends UserAction {

    RelationResponse relationResponse;

    public UserRelationActionResponse(Action action){
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
