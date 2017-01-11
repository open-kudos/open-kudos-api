package kudos.services;

import com.google.common.base.Strings;
import kudos.exceptions.UserException;
import kudos.model.ShopItem;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.model.status.TransactionStatus;
import kudos.model.status.TransactionType;
import kudos.model.status.UserStatus;
import kudos.repositories.OrderRepository;
import kudos.repositories.ShopRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public ShopItem getItem(String itemId) {
        return shopRepository.findOne(itemId);
    }

    public Page<ShopItem> getAvailableItems(Pageable pageable) {
        return shopRepository.findShopItemsByAmountGreaterThan(0, pageable);
    }

    public void addItem(ShopItem item) {
        shopRepository.insert(item);
    }

    public void removeItem(String itemId) {
        shopRepository.delete(itemId);
    }

    public void editItem(String itemId, String name, Integer price, String description, Integer amount, String pictureUrl) {
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

        shopRepository.save(oldItem);
    }

    public int calculateAvailablePoints(User user){
        return user.getTotalKudos() - transactionRepository.findTransactionBySenderAndType(user, TransactionType.SHOP)
                                        .stream().mapToInt(Transaction::getAmount).sum();
    }

    public ShopItem buyItem(String itemId, User customer) throws UserException {
        ShopItem shopItem = shopRepository.findShopItemById(itemId);

        if (calculateAvailablePoints(customer) < shopItem.getPrice())
            throw new UserException("Not enough points");

        transactionRepository.insert(new Transaction(customer, getMasterOfKudos(), shopItem.getPrice(), shopItem.getName(), "", TransactionType.SHOP, DateTime.now().toString(), TransactionStatus.SHOP));
        return shopItem;
    }


    private User getMasterOfKudos(){
        return userRepository.findByEmail("master@openkudos.com").isPresent() ?
                userRepository.findByEmail("master@openkudos.com").get() : userRepository.insert(new User("Master", "Kudos", "", "master@openkudos.com", UserStatus.MASTER));
    }

}
