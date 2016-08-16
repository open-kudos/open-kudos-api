package kudos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Action {

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

    @DBRef
    ShopItem shopItem;

    private String timestamp;

    private ActionType type;

    public Action() {}

    public Action(String id, User user, Transaction transaction, Challenge challenge, Comment comment, Relation relation, Idea idea, ShopItem shopItem, String timestamp, ActionType type) {
        this.id = id;
        this.user = user;
        this.transaction = transaction;
        this.challenge = challenge;
        this.comment = comment;
        this.relation = relation;
        this.idea = idea;
        this.shopItem = shopItem;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Action(String id, User user, Transaction transaction, Challenge challenge, Comment comment, Relation relation, Idea idea, String timestamp, ActionType type) {
        this.id = id;
        this.user = user;
        this.transaction = transaction;
        this.challenge = challenge;
        this.comment = comment;
        this.relation = relation;
        this.idea = idea;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Action(User user, Challenge challenge, String timestamp, ActionType type) {
        this.user = user;
        this.challenge = challenge;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Action(User user, Transaction transaction, String timestamp, ActionType type) {
        this.user = user;
        this.transaction = transaction;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Action(User user, Comment comment, String timestamp, ActionType type) {
        this.user = user;
        this.comment = comment;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Action(User user, Relation relation, String timestamp, ActionType type) {
        this.user = user;
        this.relation = relation;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Action(User user, Idea idea, String timestamp, ActionType type) {
        this.user = user;
        this.idea = idea;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Action(User user, ShopItem shopItem, String timestamp, ActionType type) {
        this.user = user;
        this.shopItem = shopItem;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Action(User user, String timestamp, ActionType type) {
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

    public ShopItem getShopItem() {
        return shopItem;
    }

    public void setShopItem(ShopItem shopItem) {
        this.shopItem = shopItem;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }
}
