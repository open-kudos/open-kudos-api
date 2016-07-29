package kudos.repositories;

import kudos.model.ShopItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopRepository extends MongoRepository<ShopItem,String>  {

    Page<ShopItem> findShopItemsByAmountGreaterThan(int amount, Pageable pageable);
//    ShopItem save(ShopItem item);
//    void delete(String itemId);
//    ShopItem insert(ShopItem item);
//    ShopItem findOne(String itemId);

}
