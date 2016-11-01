package kudos.model;

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

}
