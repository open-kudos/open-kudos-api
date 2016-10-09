package kudos.web.controllers;

import kudos.exceptions.UserException;
import kudos.model.ActionType;
import kudos.model.ShopItem;
import kudos.model.User;
import kudos.web.beans.response.ShopItemResponse;
import org.jsondoc.core.annotation.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(description = "service to manage web shop", name = "Shop Controller")
@RestController
@RequestMapping("/shop")
public class ShopController extends BaseController {

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public Page<ShopItemResponse> items(@RequestParam(value="page") int page,
                                        @RequestParam(value="size") int size) throws UserException {
        return convert(shopService.getItemsAvailableInShopForBuying(new PageRequest(page, size)));
    }

    @RequestMapping(value = "/{itemId}/buy", method = RequestMethod.POST)
    public void buy(@PathVariable String itemId) throws UserException {
        User user = authenticationService.getLoggedInUser();
        actionsService.save(user, shopService.getShopItem(itemId), ActionType.PURCHASED_SHOP_ITEM);
        //shopService.buyItemFromShop(user, itemId);
    }

//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    public ShopItemResponse add(@Valid @RequestBody AddShopItemForm form) throws UserException {
//        shopService.addItemToShop(new ShopItem(name, price, description, amount, pictureUrl));
//    }

    @RequestMapping(value = "/{itemId}/delete", method = RequestMethod.POST)
    public void delete(@PathVariable String itemId) throws UserException {
        shopService.removeItemFromShop(itemId);
    }

//    @RequestMapping(value = "/{itemId}/edit", method = RequestMethod.POST)
//    public ShopItemResponse edit(@PathVariable String itemId,
//                                 @RequestBody EditShopItemForm form) throws UserException {
//        return shopService.editItemOnShop(itemId, name, price, description, amount, pictureUrl);
//    }

    public Page<ShopItemResponse> convert(Page<ShopItem> items) throws UserException {
        List<ShopItemResponse> response = new ArrayList<>();

        for(ShopItem item : items.getContent()) {
            response.add(new ShopItemResponse(item));
        }
        return new PageImpl<>(response, new PageRequest(items.getNumber(), items.getSize()), items.getTotalElements());
    }
}
