package com.develhope.spring.features.users;

import com.develhope.spring.features.orders.OrderEntity;
import com.develhope.spring.features.orders.OrderStatus;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.orders.OrderService;
import com.develhope.spring.features.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/vehicle/get/{id}")
    public VehicleEntity getDetailsOfVehicle(@PathVariable long id) {
        return vehicleService.getDetailsOfVehicle(id);
    }

    @PostMapping("/order/create/{id}")
    public OrderEntity createOrderFromVehicle(@PathVariable long id, @RequestBody OrderEntity orderEntity) {
        return orderService.createOrderFromVehicle(orderEntity, id);
    }

    @DeleteMapping("/order/delete/{id}")
    public Boolean deleteOrder(@PathVariable long id) {
        return orderService.deleteOrder(id);
    }

    @PutMapping("/order/update/{id}")
    public OrderEntity updateOrder(@PathVariable long id, @RequestBody OrderEntity orderEntity) {
        return orderService.updateOrder(id, orderEntity);
    }

    @GetMapping("/order/status/get/{id}")
    public OrderStatus getOrderStatusFromId(@PathVariable long id) {
        return orderService.getOrderStatusFromId(id);
    }

    @PatchMapping("/order/status/update/{id}")
    public OrderEntity updateOrderStatusFromId(@PathVariable long id, @RequestParam String status) {
        return orderService.updateOrderStatusFromId(id, status);
    }

    @GetMapping("/order/get/by/status")
    public List<OrderEntity> getOrdersByStatus(String status) {
        return orderService.findByStatus(status);
    }

    @GetMapping("/vehicles/get")
    public List<VehicleEntity> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}
