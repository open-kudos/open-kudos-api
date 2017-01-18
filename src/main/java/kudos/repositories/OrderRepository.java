package kudos.repositories;

import kudos.model.Order;
import kudos.model.OrderStatus;
import kudos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order,String> {

    Page<Order> findOrdersByCustomerAndStatus(User customer, OrderStatus status, Pageable pageable);

    Page<Order> findOrdersByStatus(OrderStatus status, Pageable pageable);

    Optional<Order> findOrderById(String id);

}
