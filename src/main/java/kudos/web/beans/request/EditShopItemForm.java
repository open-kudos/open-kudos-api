package kudos.web.beans.request;

public class EditShopItemForm {

    private String name;
    private Integer price;
    private String description;
    private Integer amount;
    private String pictureUrl;

    public EditShopItemForm() {}

    public EditShopItemForm(String name, Integer price, String description, Integer amount, String pictureUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.amount = amount;
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
