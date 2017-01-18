package kudos.web.controllers;

import kudos.exceptions.FormValidationException;
import kudos.exceptions.UserException;
import kudos.model.InventoryItem;
import kudos.web.beans.request.AddInventoryItemForm;
import kudos.web.beans.request.validator.AddInventoryItemFormValidator;
import kudos.web.beans.response.ShopItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController extends BaseController {

    @Autowired
    AddInventoryItemFormValidator addInventoryItemFormValidator;

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public Page<ShopItemResponse> items(@RequestParam(value="page") int page,
                                        @RequestParam(value="size") int size) throws UserException {
        return convert(inventoryService.getItemsAvailableInShopForBuying(new PageRequest(page, size)));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestBody AddInventoryItemForm form, BindingResult errors) throws UserException, FormValidationException {
        addInventoryItemFormValidator.validate(form, errors);
        if(errors.hasErrors())
            throw new FormValidationException(errors);

        inventoryService.addItemToInventory(new InventoryItem(form.getName(), form.getPrice(), form.getDescription(), form.getAmount(), form.getPictureUrl()));
    }

    @RequestMapping(value = "/{itemId}/delete", method = RequestMethod.POST)
    public void delete(@PathVariable String itemId) throws UserException {
        inventoryService.removeItemFromInventory(itemId);
    }

//    @RequestMapping(value = "/{itemId}/edit", method = RequestMethod.POST)
//    public ShopItemResponse edit(@PathVariable String itemId,
//                                 @RequestBody EditInventoryItemForm form) throws UserException {
//        return inventoryService.editItemOnInventory(itemId, name, price, description, amount, pictureUrl);
//    }

    private Page<ShopItemResponse> convert(Page<InventoryItem> items) throws UserException {
        List<ShopItemResponse> response = new ArrayList<>();

        for(InventoryItem inventoryItem : items.getContent()) {
            response.add(new ShopItemResponse(inventoryItem));
        }
        return new PageImpl<>(response, new PageRequest(items.getNumber(), items.getSize()), items.getTotalElements());
    }
}
