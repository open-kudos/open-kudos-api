package kudos.model;

import org.jsondoc.core.annotation.ApiObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ApiObject
@Document
public class Idea {

    @Id
    private String id;
    private String authorName;
    private String postedByEmail;
    private String idea;

    public Idea(String authorName, String postedByEmail, String idea) {
        this.authorName = authorName;
        this.postedByEmail = postedByEmail;
        this.idea = idea;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPostedBy() {
        return postedByEmail;
    }

    public String getIdea() {
        return idea;
    }

}
