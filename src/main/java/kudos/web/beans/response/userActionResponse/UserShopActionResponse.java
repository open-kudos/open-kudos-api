package kudos.web.beans.response.userActionResponse;

import kudos.model.Action;
import kudos.web.beans.response.ShopItemResponse;

public class UserShopActionResponse extends UserAction {

    ShopItemResponse shopItemResponse;

    public UserShopActionResponse(Action action){
        super(action);
        this.shopItemResponse = new ShopItemResponse(action.getShopItem());
    }

    public ShopItemResponse getShopItemResponse() {
        return shopItemResponse;
    }

    public void setShopItemResponse(ShopItemResponse shopItemResponse) {
        this.shopItemResponse = shopItemResponse;
    }
}
