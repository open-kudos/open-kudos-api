package kudos.model;

import org.jsondoc.core.annotation.ApiObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ApiObject
@Document
public class Idea {

    @Id
    private String id;
    private String authorEmail;
    private String postedByEmail;
    private String idea;

    public Idea(String authorEmail, String postedByEmail, String idea) {
        this.authorEmail = authorEmail;
        this.postedByEmail = postedByEmail;
        this.idea = idea;
    }

    public String getAuthorOfIdea() {
        return authorEmail;
    }

    public String getPostedBy() {
        return postedByEmail;
    }

    public String getIdea() {
        return idea;
    }

}
