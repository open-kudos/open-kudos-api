package kudos.services;

import kudos.model.Order;
import kudos.model.OrderStatus;
import kudos.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    public OrderRepository orderRepository;

    public Order newOrder(Order order){
        return orderRepository.insert(order);
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

}
