package kudos.web.controllers;

import kudos.exceptions.UserException;
import kudos.model.status.ActionType;
import kudos.model.ShopItem;
import kudos.model.User;
import kudos.web.beans.request.AddShopItemForm;
import kudos.web.beans.request.EditShopItemForm;
import kudos.web.beans.response.PointsResponse;
import kudos.web.beans.response.ShopItemResponse;
import org.jsondoc.core.annotation.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop")
public class ShopController extends BaseController {

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public Page<ShopItemResponse> fetchItems(@RequestParam(value="page") int page, @RequestParam(value="size") int size) {
        return convert(shopService.getAvailableItems(new PageRequest(page, size)));
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public ShopItemResponse fetchItem(@PathVariable String id){
        return new ShopItemResponse(shopService.getItem(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addItem(@RequestBody AddShopItemForm form){
        ShopItem itemToAdd = new ShopItem(form.getName(), form.getPrice(), form.getDescription(), form.getAmount(), form.getPictureUrl());
        shopService.addItem(itemToAdd);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public void editItem(@PathVariable String id, EditShopItemForm form){
        shopService.editItem(id, form.getName(), form.getPrice(), form.getDescription(), form.getAmount(), form.getPictureUrl());
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public void removeItem(@PathVariable String id){
        shopService.removeItem(id);
    }

    @RequestMapping(value = "/buy/{id}", method = RequestMethod.POST)
    public void buyItem(@PathVariable String id) throws UserException {
        User user = authenticationService.getLoggedInUser();
        ShopItem shopItem = shopService.buyItem(id, user);
        ordersService.newOrder(user, shopItem);
    }

    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public PointsResponse calculateUserAvailableKudosPoints() throws UserException {
        User user = authenticationService.getLoggedInUser();
        return new PointsResponse(shopService.calculateAvailablePoints(user));
    }

    private Page<ShopItemResponse> convert(Page<ShopItem> items) {
        List<ShopItemResponse> response = items.getContent().stream().map(ShopItemResponse::new).collect(Collectors.toList());
        return new PageImpl<>(response, new PageRequest(items.getNumber(), items.getSize()), items.getTotalElements());
    }
}
