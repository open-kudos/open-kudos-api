package kudos.web.controllers;

import kudos.exceptions.InvalidItemAmountException;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.OrderException;
import kudos.exceptions.UserException;
import kudos.model.*;
import kudos.web.beans.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @RequestMapping(value = "/place/{itemId}", method = RequestMethod.POST)
    public Order placeOrder(@PathVariable String itemId) throws UserException, InvalidItemAmountException, InvalidKudosAmountException {
        User user = authenticationService.getLoggedInUser();
        return orderService.placeOrder(user, itemId);
    }

    @RequestMapping(value = "/{orderId}/approve", method = RequestMethod.POST)
    public Order approveOrder(@PathVariable String orderId) throws UserException, OrderException {
        User user = authenticationService.getLoggedInUser();
        InventoryItem inventoryItem = orderService.getOrderById(orderId).getInventoryItem();
        actionsService.save(user, inventoryItem, ActionType.PURCHASED_SHOP_ITEM);
        return orderService.approveOrder(orderId);
    }

    @RequestMapping(value = "/{orderId}/cancel", method = RequestMethod.POST)
    public void cancelOrder(@PathVariable String orderId) throws UserException, OrderException {
        orderService.cancelOrder(orderId);
    }

    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public Page<OrderResponse> getAllPendingOrders(@RequestParam int page, @RequestParam int size){
        return convert(orderService.getOrdersForApproval(new PageRequest(page, size)));
    }

    @RequestMapping(value = "/placed", method = RequestMethod.GET)
    public Page<Order> getOrdersPlacedByUser(@RequestParam int page,
                                         @RequestParam int size) throws UserException {
        User user = authenticationService.getLoggedInUser();
        return orderService.getOrdersPlacedByUser(user, new PageRequest(page, size));
    }

    public Page<OrderResponse> convert(Page<Order> orders) {
        List<OrderResponse> response = new ArrayList<>();

        for(Order item : orders.getContent()) {
            response.add(new OrderResponse(item));
        }
        return new PageImpl<>(response, new PageRequest(orders.getNumber(), orders.getSize()),
                orders.getTotalElements());
    }

}
