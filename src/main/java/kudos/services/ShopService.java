package kudos.services;


import com.google.common.base.Strings;
import kudos.exceptions.UserException;
import kudos.model.ShopItem;
import kudos.model.User;
import kudos.repositories.ShopRepository;
import kudos.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Page<ShopItem> getItemsAvailableInShopForBuying(Pageable pageable) {
        return shopRepository.findShopItemsByAmountGreaterThan(0, pageable);
    }

    public void addItemToShop(ShopItem item) {
        shopRepository.insert(item);
    }

    public void removeItemFromShop(String itemId) {
        shopRepository.delete(itemId);
    }

    public ShopItem getShopItem(String itemId) {
        return shopRepository.findOne(itemId);
    }

    public ShopItem editItemOnShop(String itemId, String name, Integer price, String description, Integer amount, String pictureUrl) {
        ShopItem oldItem = shopRepository.findOne(itemId);

        if(!Strings.isNullOrEmpty(name))
            oldItem.setName(name);

        if(!Strings.isNullOrEmpty(description))
            oldItem.setDescription(description);

        if(price != null && price > 0)
            oldItem.setPrice(price);

        if(amount != null && amount > 0)
            oldItem.setAmount(amount);

        if(!Strings.isNullOrEmpty(pictureUrl))
            oldItem.setPictureUrl(pictureUrl);

        return shopRepository.save(oldItem);
    }

    public void buyItemFromShop(String itemId, User user) throws UserException {
//        ShopItem item = shopRepository.findOne(itemId);
//
//        //kudosService.takeSystemKudos(user, item.getPrice()*-1, "Buying: " + item.getName(), Transaction.Status.SHOP);
//
//        item.setAmount(item.getAmount() - 1);
//        shopRepository.save(item);
//
//        try {
//            Transaction newTransaction = new Transaction(kudosMaster, user, item.getPrice() * -1, "Buying: " + item.getName(), Transaction.Status.SHOP);
//
//            if (kudosService.getFreeKudos(user) < item.getPrice()) {
//                throw new KudosExceededException("exceeded_kudos");
//            }
//
//            newTransaction.setReceiverBalance(kudosService.getKudos(user) - item.getPrice());
//
//            transactionRepository.insert(newTransaction);
//        } catch (Exception e) {
//            item.setAmount(item.getAmount() + 1);
//            shopRepository.save(item);
//        }
    }


}
