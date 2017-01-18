package kudos.model;

import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Order {

    @Id
    private String id;
    @DBRef
    private User customer;
    @DBRef
    private InventoryItem inventoryItem;
    private OrderStatus status;
    @DBRef
    private Transaction transaction;

    public Order(User customer, InventoryItem inventoryItem, OrderStatus status, Transaction transaction) {
        this.customer = customer;
        this.inventoryItem = inventoryItem;
        this.status = status;
        this.transaction = transaction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
