package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.orders.dto.PatchOrderRequest;
import com.develhope.spring.features.orders.dto.TotalSalesPricePeriodRequest;
import com.develhope.spring.features.users.Role;
import com.develhope.spring.features.users.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    public static final String ORDER_PATH = "/orders";
    public static final String ORDER_PATH_ID = ORDER_PATH + "/{orderId}";

    private final OrderService orderService;

    //this creates an order that has to be yet finalized, because the vehicle is not ready
    //for delivery
    @PutMapping(path = ORDER_PATH + "/prepare")
    public ResponseEntity<?> prepareOrderByVehicleId(@AuthenticationPrincipal UserEntity user,
                                                     @RequestBody CreateOrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.prepareOrderByVehicleId(user, orderRequest);
        if (orderResponse == null) {
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    //this creates an order that is ready to be delivered
    @PutMapping(path = ORDER_PATH + "/create")
    public ResponseEntity<?> createOrderByVehicleId(@AuthenticationPrincipal UserEntity user,
                                                    @RequestBody CreateOrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrderByVehicleId(user, orderRequest);
        if (orderResponse == null) {
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    @PatchMapping(path = ORDER_PATH_ID)
    public OrderResponse patchOrder(@AuthenticationPrincipal UserEntity user,
                                    @PathVariable Long orderId,
                                    @RequestBody PatchOrderRequest patchOrderRequest) {
        return orderService.patchOrder(user, orderId, patchOrderRequest);
    }

    @PatchMapping(path = ORDER_PATH_ID + "/status")
    public OrderResponse patchOrderStatus(@AuthenticationPrincipal UserEntity user,
                                          @PathVariable Long orderId,
                                          @RequestParam String status) {
        return orderService.patchOrderStatus(user, orderId, status);
    }

    //this is a list of orders that the user has made
    @GetMapping(path = ORDER_PATH + "/byuser/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@AuthenticationPrincipal UserEntity user,
                                               @PathVariable Long userId) {
        return orderService.getOrderListByBuyerId(user, userId);
    }

    @GetMapping(path = ORDER_PATH + "/{buyerId}/complete")
    public ResponseEntity<?> getOrdersCompletedListByBuyerId(@AuthenticationPrincipal UserEntity user,
                                                             @PathVariable Long buyerId) {
        return orderService.getOrdersCompletedListByBuyerId(user, buyerId);
    }

    //this is a list of the orders that have a certain status
    @GetMapping(path = ORDER_PATH + "/bystatus")
    public ResponseEntity<?> getOrdersByStatus(@AuthenticationPrincipal UserEntity user,
                                               @RequestParam String status) {
        return orderService.getOrdersByStatus(user, status);
    }

    @GetMapping(path = ORDER_PATH_ID + "/status")
    public ResponseEntity<?> getOrderStatus(@AuthenticationPrincipal UserEntity user,
                                            @PathVariable Long orderId) {
        OrderStatus orderStatus = orderService.getOrderStatus(user, orderId);
        if (orderStatus == null) {
            return new ResponseEntity<>(orderStatus, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(orderStatus, HttpStatus.OK);
    }

    @DeleteMapping(path = ORDER_PATH_ID)
    public Boolean deleteOrder(@AuthenticationPrincipal UserEntity user,
                               @PathVariable Long orderId) {
        return orderService.deleteOrder(user, orderId);
    }

    //ADMIN ROUTES
    @GetMapping(path = ORDER_PATH + "/totalsalespriceperiod")
    public ResponseEntity<?> getTotalSalesPriceInAPeriod(@AuthenticationPrincipal UserEntity user, @RequestBody TotalSalesPricePeriodRequest request) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return orderService.getTotalSalesPriceInAPeriod(request.getStartDate(), request.getEndDate());
    }

}
