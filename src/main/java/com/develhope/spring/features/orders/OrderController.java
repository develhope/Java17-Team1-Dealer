package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.orders.dto.PatchOrderRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    public static final String ORDER_PATH = "/orders";
    public static final String ORDER_PATH_ID = ORDER_PATH + "/{orderId}";
    public static final String ORDER_CREATION_PATH = ORDER_PATH + "/vehicle/{vehicleId}";

    private final OrderService orderService;

    //this creates an order that has to be yet finalized, because the vehicle is not ready
    //for delivery
    @PutMapping(path = ORDER_CREATION_PATH + "/prepare")
    public ResponseEntity<?> prepareOrderByVehicleId(@PathVariable Long vehicleId, @RequestBody CreateOrderRequest orderRequest, @RequestParam(required = true) Long requester_id) {
        OrderResponse orderResponse = orderService.prepareOrderByVehicleId(vehicleId, orderRequest, requester_id);
        if (orderResponse == null) {
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    //this creates an order that is ready to be delivered
    @PutMapping(path = ORDER_CREATION_PATH + "/create")
    public ResponseEntity<?> createOrderByVehicleId(@PathVariable Long vehicleId, @RequestBody CreateOrderRequest orderRequest, @RequestParam(required = true) Long requester_id) {
        OrderResponse orderResponse = orderService.prepareOrderByVehicleId(vehicleId, orderRequest, requester_id);
        if (orderResponse == null) {
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    @PutMapping(path = ORDER_PATH_ID)
    public OrderEntity patchOrder(@PathVariable Long orderId, @RequestBody PatchOrderRequest patchOrderRequest, @RequestParam(required = true) Long requester_id) {
        return orderService.patchOrder(orderId, patchOrderRequest, requester_id);
    }

    @PatchMapping(path = ORDER_PATH_ID + "/status")
    public OrderEntity patchOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.patchOrderStatus(orderId, status);
    }

    //this is a list of orders that the user has made
    @GetMapping(path = ORDER_PATH + "/byuser/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Long userId, @RequestParam(required = true) Long requester_id) {

        List<OrderResponse> orderResponseList = orderService.getOrderListById(userId, requester_id);
        System.out.println(orderResponseList);
        return new ResponseEntity<>(orderResponseList, HttpStatus.OK);
    }

    //this is a list of the orders that have a certain status
    @GetMapping(path = ORDER_PATH + "/bystatus")
    public List<OrderEntity> getOrdersByStatus(@RequestParam String status, @RequestParam(required = true) Long requester_id) {
        return orderService.findByStatus(status, requester_id);
    }

    @DeleteMapping(path = ORDER_PATH_ID)
    public Boolean deleteOrder(@PathVariable Long orderId, @RequestParam(required = true) Long requester_id) {
        return orderService.deleteOrder(orderId, requester_id);
    }
}
