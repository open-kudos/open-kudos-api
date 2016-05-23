package kudos.model;

import org.jsondoc.core.annotation.ApiObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ApiObject
@Document
public class WisdomWall {

    @Id
    private String id;
    private String authorOfIdea;
    private String postedBy;
    private String idea;
    private String timestamp;

    public WisdomWall(String authorOfIdea, String postedBy, String idea, String timestamp) {
        this.authorOfIdea = authorOfIdea;
        this.postedBy = postedBy;
        this.idea = idea;
        this.timestamp = timestamp;
    }

    public String getAuthorOfIdea() {
        return authorOfIdea;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public String getIdea() {
        return idea;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
