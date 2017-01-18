package kudos.services;

import kudos.exceptions.InvalidItemAmountException;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.OrderException;
import kudos.exceptions.UserException;
import kudos.model.*;
import kudos.repositories.InventoryRepository;
import kudos.repositories.OrderRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public Order placeOrder(User customer, String itemId) throws InvalidKudosAmountException, InvalidItemAmountException, UserException {
        InventoryItem inventoryItem = inventoryRepository.findOne(itemId);

        if(inventoryItem.getAmount() < 1)
            throw new InvalidItemAmountException("invalid_item_amount");

        if(customer.getTotalKudos() < inventoryItem.getPrice())
            throw new InvalidKudosAmountException("invalid_kudos_amount");

        inventoryItem.setAmount(inventoryItem.getAmount() - 1);
        inventoryRepository.save(inventoryItem);

        Transaction transaction = transactionRepository.save(new Transaction(customer, getAdmin(), inventoryItem.getPrice(), "buying:" + inventoryItem.getName(),
                TransactionType.SHOP, LocalDateTime.now().toString(), TransactionStatus.PENDING));

        return orderRepository.insert(new Order(customer, inventoryItem, OrderStatus.PENDING, transaction));
    }

    public Order approveOrder(String orderId) throws UserException, OrderException {
        Order orderToUpdate = orderRepository.findOne(orderId);

        if(!orderToUpdate.getStatus().equals(OrderStatus.PENDING))
            throw new OrderException("cannot_approve_order");

        Transaction transaction = orderToUpdate.getTransaction();
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);

        User user = orderToUpdate.getCustomer();
        user.setTotalKudos(user.getTotalKudos() - transaction.getAmount());
        userRepository.save(user);

        User admin = getAdmin();
        admin.setTotalKudos(admin.getTotalKudos() + transaction.getAmount());
        userRepository.save(admin);

        orderToUpdate.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(orderToUpdate);
    }

    public void cancelOrder(String orderId) throws UserException, OrderException {
        Order orderToCancel = orderRepository.findOne(orderId);

        if(!orderToCancel.getStatus().equals(OrderStatus.PENDING))
            throw new OrderException("cannot_cancel_order");

        User user = orderToCancel.getCustomer();
        user.setTotalKudos(user.getTotalKudos() + orderToCancel.getTransaction().getAmount());
        userRepository.save(user);

        User admin = getAdmin();
        admin.setTotalKudos(admin.getTotalKudos() - orderToCancel.getTransaction().getAmount());
        userRepository.save(admin);

        transactionRepository.delete(orderToCancel.getTransaction());
        orderRepository.delete(orderToCancel);
    }

    public Page<Order> getOrdersForApproval(Pageable pageable){
        return orderRepository.findOrdersByStatus(OrderStatus.PENDING, pageable);
    }

    public Page<Order> getOrdersPlacedByUser(User user, Pageable pageable){
        return orderRepository.findOrdersByCustomerAndStatus(user, OrderStatus.PENDING, pageable);
    }

    private User getAdmin() throws UserException {
        Optional<User> admin = userRepository.findByRole(UserRole.ROLE_ADMIN.name());

        if(!admin.isPresent())
            throw new UserException("user_not_found");

        return admin.get();
    }

    public Order getOrderById(String id) throws UserException {
        Optional<Order> order = orderRepository.findOrderById(id);
        if(order.isPresent()) {
            return order.get();
        } else {
            throw new UserException("order_not_found");
        }
    }

}
