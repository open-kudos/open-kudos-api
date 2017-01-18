package kudos.repositories;

import kudos.model.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<InventoryItem,String>  {

    Page<InventoryItem> findInventoryItemsByAmountGreaterThan(int amount, Pageable pageable);

//    InventoryItem save(InventoryItem item);
//    void delete(String itemId);
//    InventoryItem insert(InventoryItem item);
//    InventoryItem findOne(String itemId);

}
