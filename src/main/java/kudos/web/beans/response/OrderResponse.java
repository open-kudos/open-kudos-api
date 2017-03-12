package kudos.web.beans.response;

import kudos.model.Order;
import kudos.model.ShopItem;
import kudos.model.User;
import kudos.model.status.OrderStatus;

public class OrderResponse {

    private UserResponse userResponse;
    private ShopItemResponse shopItemResponse;

    private OrderStatus status;
    private String timeStamp;

    public OrderResponse(Order order){
        this.userResponse = new UserResponse(order.getCustomer());
        this.shopItemResponse = new ShopItemResponse(order.getItem());
        this.status = order.getStatus();
        this.timeStamp = order.getTimestamp();
    }

    public OrderResponse(User user, ShopItem shopItem, OrderStatus status, String timeStamp){
        this.userResponse = new UserResponse(user);
        this.shopItemResponse = new ShopItemResponse(shopItem);
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public ShopItemResponse getShopItemResponse() {
        return shopItemResponse;
    }

    public void setShopItemResponse(ShopItemResponse shopItemResponse) {
        this.shopItemResponse = shopItemResponse;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
