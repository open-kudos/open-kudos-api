package kudos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Feed {

    @Id
    private String id;

    @DBRef
    User user;

    @DBRef
    Transaction transaction;

    @DBRef
    Challenge challenge;

    @DBRef
    Comment comment;

    @DBRef
    Relation relation;

    @DBRef
    Idea idea;

    private String timestamp;

    private FeedType type;

    public Feed(User user, Challenge challenge, String timestamp, FeedType type) {
        this.user = user;
        this.challenge = challenge;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Feed(User user, Transaction transaction, String timestamp, FeedType type) {
        this.user = user;
        this.transaction = transaction;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Feed(User user, Comment comment, String timestamp, FeedType type) {
        this.user = user;
        this.comment = comment;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Feed(User user, Relation relation, String timestamp, FeedType type) {
        this.user = user;
        this.comment = comment;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Feed(User user, Idea idea, String timestamp, FeedType type) {
        this.user = user;
        this.idea = idea;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Feed(User user, String timestamp, FeedType type) {
        this.user = user;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public FeedType getType() {
        return type;
    }

    public void setType(FeedType type) {
        this.type = type;
    }
}
