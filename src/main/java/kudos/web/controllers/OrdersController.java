package kudos.web.controllers;

import com.sun.org.apache.xpath.internal.operations.Or;
import kudos.exceptions.UserException;
import kudos.model.Order;
import kudos.model.Transaction;
import kudos.model.status.OrderStatus;
import kudos.model.User;
import kudos.web.beans.response.KudosTransactionResponse;
import kudos.web.beans.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrdersController extends BaseController {

    @RequestMapping(value = "/update/{orderId}", method = RequestMethod.POST)
    public void updateOrder(@PathVariable String orderId,
                            @RequestParam OrderStatus orderStatus){
        ordersService.updateOrderStatus(orderId, orderStatus);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Page<Order> getAllOrders(@RequestParam int page,
                                    @RequestParam int size){
        return ordersService.getPageableOrders(new PageRequest(page, size));
    }

    @RequestMapping(value = "/get/{userId}", method = RequestMethod.GET)
    public Page<Order> getOrdersByUserId(@PathVariable String userId,
                                         @RequestParam int page,
                                         @RequestParam int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return ordersService.getPageableOrdersByUser(user, new PageRequest(page, size));
    }

    private Page<OrderResponse> convert(Page<Order> input) {
        List<OrderResponse> orders = input.getContent().stream().map(OrderResponse::new).collect(Collectors.toList());
        return new PageImpl<OrderResponse>(orders, new PageRequest(input.getNumber(), input.getSize()), input.getTotalElements());
    }

}
