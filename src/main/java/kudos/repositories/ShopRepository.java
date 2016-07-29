package kudos.repositories;

import kudos.model.ShopItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopRepository extends MongoRepository<ShopItem,String>  {

//    Page<ShopItem> findAll();
//    ShopItem save(ShopItem item);
//    void delete(String itemId);
//    ShopItem insert(ShopItem item);
//    ShopItem findOne(String itemId);

}
