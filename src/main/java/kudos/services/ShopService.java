package kudos.services;


import com.google.common.base.Strings;
import kudos.exceptions.BusinessException;
import kudos.exceptions.KudosExceededException;
import kudos.model.ShopItem;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.ShopRepository;
import kudos.repositories.TransactionRepository;
import kudos.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShopService {

//    @Autowired
//    private ShopRepository shopRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private UsersService usersService;
//
//    @Autowired
//    private KudosService kudosService;
//
//    public List<ShopItem> availableItemsInShop() {
//        return shopRepository.findAll();
//    }
//
//    public void addItemToShop(ShopItem item) {
//        shopRepository.insert(item);
//    }
//
//    public void removeItemFromShop(String itemId) {
//        shopRepository.delete(itemId);
//    }
//
//    public ShopItem editItemOnShop(String itemId, String name, Integer price, String description, Integer amount, String pictureUrl) {
//        ShopItem oldItem = shopRepository.findOne(itemId);
//
//        if(!Strings.isNullOrEmpty(name))
//            oldItem.setName(name);
//
//        if(!Strings.isNullOrEmpty(description))
//            oldItem.setDescription(description);
//
//        if(price != null && price > 0)
//            oldItem.setPrice(price);
//
//        if(amount != null && amount > 0)
//            oldItem.setAmount(amount);
//
//        if(!Strings.isNullOrEmpty(pictureUrl))
//            oldItem.setPictureUrl(pictureUrl);
//
//        return shopRepository.save(oldItem);
//    }
//
//    public void buyItemFromShop(String itemId) throws UserException, BusinessException {
//        User kudosMaster = usersService.getKudosMaster();
//        User user = usersService.getLoggedUser().get();
//
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
//    }


}
