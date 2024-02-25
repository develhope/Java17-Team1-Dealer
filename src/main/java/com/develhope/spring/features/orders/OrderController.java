package com.develhope.spring.features.orders;

import com.develhope.spring.features.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{id}")
    public OrderEntity createOrderFromVehicle(@PathVariable long id, @RequestBody OrderEntity orderEntity) {
        return orderService.createOrderFromVehicle(orderEntity, id);
    }

    @PutMapping("/update/{id}")
    public OrderEntity updateOrder(@PathVariable long id, @RequestBody OrderEntity orderEntity) {
        return orderService.updateOrder(id, orderEntity);
    }

    @PatchMapping("/update/status/{id}")
    public OrderEntity updateOrderStatusFromId(@PathVariable long id, @RequestParam String status) {
        return orderService.updateOrderStatusFromId(id, status);
    }

    @GetMapping("/status/get/{id}")
    public OrderStatus getOrderStatusFromId(@PathVariable long id) {
        return orderService.getOrderStatusFromId(id);
    }

    @GetMapping("/by/status")
    public List<OrderEntity> getOrdersByStatus(String status) {
        return orderService.findByStatus(status);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteOrder(@PathVariable long id) {
        return orderService.deleteOrder(id);
    }
}
