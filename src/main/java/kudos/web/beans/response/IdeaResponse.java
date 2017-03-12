package kudos.web.beans.response;

import kudos.model.Idea;

public class IdeaResponse extends Response {

    private String creatorId;
    private String author;
    private String phrase;

    public IdeaResponse(Idea idea){
        this.creatorId = idea.getCreator().getId();
        this.author = idea.getAuthor();
        this.phrase = idea.getPhrase();
    }

    public IdeaResponse(String creatorId, String author, String phrase) {
        this.creatorId = creatorId;
        this.author = author;
        this.phrase = phrase;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}
