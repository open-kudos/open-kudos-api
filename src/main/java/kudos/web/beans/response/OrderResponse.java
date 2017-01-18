package kudos.web.beans.response;

import kudos.model.Order;

public class OrderResponse extends Response {

    private String orderId;
    private String creatorFullName;
    private String itemName;
    private String pictureUrl;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.creatorFullName = order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName();
        this.itemName = order.getInventoryItem().getName();
        this.pictureUrl = order.getInventoryItem().getPictureUrl();
    }

    public OrderResponse(String orderId, String creatorFullName, String itemName, String pictureUrl) {
        this.orderId = orderId;
        this.creatorFullName = creatorFullName;
        this.itemName = itemName;
        this.pictureUrl = pictureUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
