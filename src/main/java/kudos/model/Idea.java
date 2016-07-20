package kudos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Idea {

    @Id
    private String id;
    private String authorName;
    private String postedByEmail;
    private String idea;
    private String creationDate;

    public Idea(String authorName, String postedByEmail, String idea, String creationDate) {
        this.authorName = authorName;
        this.postedByEmail = postedByEmail;
        this.idea = idea;
        this.creationDate = creationDate;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
