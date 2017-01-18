package kudos.services;


import com.google.common.base.Strings;
import kudos.model.InventoryItem;
import kudos.repositories.InventoryRepository;
import kudos.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Page<InventoryItem> getItemsAvailableInShopForBuying(Pageable pageable) {
        return inventoryRepository.findInventoryItemsByAmountGreaterThan(0, pageable);
    }

    public void addItemToInventory(InventoryItem inventoryItem) {
        inventoryRepository.insert(inventoryItem);
    }

    public void removeItemFromInventory(String itemId) {
        inventoryRepository.delete(itemId);
    }

    public InventoryItem getInventoryItem(String itemId) {
        return inventoryRepository.findOne(itemId);
    }

    public InventoryItem editItemOnInventory(String itemId, String name, Integer price, String description, Integer amount, String pictureUrl) {
        InventoryItem oldInventoryItem = inventoryRepository.findOne(itemId);

        if(!Strings.isNullOrEmpty(name))
            oldInventoryItem.setName(name);

        if(!Strings.isNullOrEmpty(description))
            oldInventoryItem.setDescription(description);

        if(price != null && price > 0)
            oldInventoryItem.setPrice(price);

        if(amount != null && amount > 0)
            oldInventoryItem.setAmount(amount);

        if(!Strings.isNullOrEmpty(pictureUrl))
            oldInventoryItem.setPictureUrl(pictureUrl);

        return inventoryRepository.save(oldInventoryItem);
    }

}
