package kudos.repositories;

import kudos.model.Order;
import kudos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order,String> {

    Page<Order> findOrdersByCustomer(User customer, Pageable pageable);

}
