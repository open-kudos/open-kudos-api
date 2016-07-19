package kudos.web.controllers;

import kudos.exceptions.BusinessException;
import kudos.model.ShopItem;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "service to manage web shop", name = "Shop Controller")
@RestController
@RequestMapping("/shop")
public class ShopController extends BaseController {

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public List<ShopItem> items() throws UserException {
        return shopService.availableItemsInShop();
    }

    @RequestMapping(value = "/buy/{itemId}", method = RequestMethod.POST)
    public void buy(@PathVariable String itemId) throws UserException, BusinessException {
        shopService.buyItemFromShop(itemId);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestParam(value="name") String name,
                              @RequestParam(value="price") int price,
                              @RequestParam(value="description") String description,
                              @RequestParam(value="amount") int amount) throws UserException {
        shopService.addItemToShop(new ShopItem(name, price, description, amount));
    }

    @RequestMapping(value = "/delete/{itemId}", method = RequestMethod.POST)
    public void delete(@PathVariable String itemId) throws UserException {
        shopService.removeItemFromShop(itemId);
    }

    @RequestMapping(value = "/edit/{itemId}", method = RequestMethod.POST)
    public ShopItem edit(@PathVariable String itemId,
                                   @RequestParam(value="name", required = false) String name,
                                   @RequestParam(value="price", required = false) int price,
                                   @RequestParam(value="description", required = false) String description,
                                   @RequestParam(value="amount", required = false) int amount) throws UserException {
        return shopService.editItemOnShop(itemId, name, price, description, amount);
    }

}
