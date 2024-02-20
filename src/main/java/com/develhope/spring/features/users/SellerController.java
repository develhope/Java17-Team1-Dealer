package com.develhope.spring.features.users;

import com.develhope.spring.features.orders.Order;
import com.develhope.spring.features.orders.OrderStatus;
import com.develhope.spring.features.vehicle.Vehicle;
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
    public Vehicle getDetailsOfVehicle(@PathVariable long id) {
        return vehicleService.getDetailsOfVehicle(id);
    }

    @PostMapping("/order/create/{id}")
    public Order createOrderFromVehicle(@PathVariable long id, @RequestBody Order order) {
        return orderService.createOrderFromVehicle(order, id);
    }

    @DeleteMapping("/order/delete/{id}")
    public Boolean deleteOrder(@PathVariable long id) {
        return orderService.deleteOrder(id);
    }

    @PutMapping("/order/update/{id}")
    public Order updateOrder(@PathVariable long id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }

    @GetMapping("/order/status/get/{id}")
    public OrderStatus getOrderStatusFromId(@PathVariable long id) {
        return orderService.getOrderStatusFromId(id);
    }

    @PatchMapping("/order/status/update/{id}")
    public Order updateOrderStatusFromId(@PathVariable long id, @RequestParam String status) {
        return orderService.updateOrderStatusFromId(id, status);
    }

    @GetMapping("/order/get/by/status")
    public List<Order> getOrdersByStatus(String status) {
        return orderService.findByStatus(status);
    }

    @GetMapping("/vehicles/get")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}
