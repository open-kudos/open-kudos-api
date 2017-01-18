package kudos.web.beans.response;

import kudos.model.InventoryItem;

public class ShopItemResponse extends Response {

    private String id;
    private String name;
    private int price;
    private String description;
    private int amount;
    private String pictureUrl;

    public ShopItemResponse(InventoryItem inventoryItem) {
        this.id = inventoryItem.getId();
        this.name = inventoryItem.getName();
        this.price = inventoryItem.getPrice();
        this.description = inventoryItem.getDescription();
        this.amount = inventoryItem.getAmount();
        this.pictureUrl = inventoryItem.getPictureUrl();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

}
