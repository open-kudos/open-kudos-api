package kudos.repositories;

import kudos.model.ShopItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ShopRepository extends MongoRepository<ShopItem,String>  {

//    List<ShopItem> findAll();
//    ShopItem save(ShopItem item);
//    void delete(String itemId);
//    ShopItem insert(ShopItem item);
//    ShopItem findOne(String itemId);

}
