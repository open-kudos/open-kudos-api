package kudos.model;

import kudos.model.status.OrderStatus;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Order {

    @Id
    private String id;
    @DBRef
    private User customer;
    @DBRef
    private ShopItem item;
    private OrderStatus status;
    private String timestamp;

    public Order(User customer, ShopItem item, OrderStatus status) {
        this.customer = customer;
        this.item = item;
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
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

    public ShopItem getItem() {
        return item;
    }

    public void setItem(ShopItem item) {
        this.item = item;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
