package kudos.services;

import kudos.model.Order;
import kudos.model.OrderStatus;
import kudos.model.ShopItem;
import kudos.model.User;
import kudos.repositories.OrderRepository;
import kudos.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    public OrderRepository orderRepository;
    public UserRepository userRepository;

    public Order newOrder(User customer, ShopItem item) {
        return orderRepository.insert(new Order(customer, item, OrderStatus.PENDING));
    }

    public Order updateOrderStatus(String orderId, OrderStatus orderStatus){
        Order orderToUpdate = orderRepository.findOne(orderId);
        orderToUpdate.setStatus(orderStatus);
        return orderRepository.save(orderToUpdate);
    }

    public void deleteOrder(String orderId){
        Order orderToRemove = orderRepository.findOne(orderId);
        orderRepository.delete(orderToRemove);
    }

    public Page<Order> getPageableOrders(Pageable pageable){
        return orderRepository.findAllOrdersOrderByTimestampDesc(pageable);
    }

    public Page<Order> getPageableOrdersByUser(User user, Pageable pageable){
        return orderRepository.findOrdersByCustomerOrderByTimestampDesc(user, pageable);
    }

}
