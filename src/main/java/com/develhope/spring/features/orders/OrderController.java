package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.OrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;

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
     @PutMapping(path = ORDER_CREATION_PATH)
    public ResponseEntity<?> prepareOrderByVehicleId(@PathVariable Long vehicleId, @RequestBody OrderRequest orderRequest, @RequestParam(required = true) Long requester_id) {
        OrderResponse orderResponse = orderService.prepareOrderByVehicleId(vehicleId, orderRequest, requester_id);
        if (orderResponse == null) {
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    } 

    //this creates an order that is ready to be delivered
    @PutMapping(path = ORDER_CREATION_PATH)
    public ResponseEntity<?> createOrderByVehicleId(@PathVariable Long vehicleId, @RequestBody OrderRequest orderRequest, @RequestParam(required = true) Long requester_id) {
        OrderResponse orderResponse = orderService.prepareOrderByVehicleId(vehicleId, orderRequest, requester_id);
        if (orderResponse == null) {
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    } 


    @PutMapping(path = ORDER_PATH_ID)
    public OrderEntity patchOrder(@PathVariable Long id, @RequestBody OrderEntity orderEntity) {
        return orderService.patchOrder(id, orderEntity);
    }

    @PatchMapping(path = ORDER_PATH_ID)
    public OrderEntity patchOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return orderService.patchOrderStatus(id, status);
    }

    @GetMapping(path = ORDER_PATH_ID)
    public OrderStatus getOrderStatusFromId(@PathVariable Long id) {
        return orderService.getOrderStatusFromId(id);
    }

    @GetMapping(path = ORDER_PATH + "/bystatus")
    public List<OrderEntity> getOrdersByStatus(String status) {
        return orderService.findByStatus(status);
    }

    @DeleteMapping(path = ORDER_PATH_ID)
    public Boolean deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
