package kudos.web.controllers;

import kudos.exceptions.UserException;
import kudos.model.Order;
import kudos.model.OrderStatus;
import kudos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController extends BaseController {

    @RequestMapping(value = "/update/{orderId}", method = RequestMethod.GET)
    public void updateOrder(@PathVariable String orderId,
                            @RequestParam OrderStatus orderStatus){
        ordersService.updateOrderStatus(orderId, orderStatus);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<Order> getAllOrders(@RequestParam int page,
                                    @RequestParam int size){
        return ordersService.getPageableOrders(new PageRequest(page, size));
    }

    @RequestMapping(value = "/all/{userId}", method = RequestMethod.GET)
    public Page<Order> getOrdersByUserId(@PathVariable String userId,
                                         @RequestParam int page,
                                         @RequestParam int size) throws UserException {
        User user = usersService.findByUserId(userId);
        return ordersService.getPageableOrdersByUser(user, new PageRequest(page, size));
    }

}
