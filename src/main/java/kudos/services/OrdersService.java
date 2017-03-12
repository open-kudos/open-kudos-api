package kudos.services;

import kudos.model.Order;
import kudos.model.status.OrderStatus;
import kudos.model.ShopItem;
import kudos.model.User;
import kudos.repositories.OrderRepository;
import kudos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    @Autowired
    private OrderRepository orderRepository;

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
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getPageableOrdersByUser(User user, Pageable pageable){
        return orderRepository.findOrdersByCustomer(user, pageable);
    }

}
