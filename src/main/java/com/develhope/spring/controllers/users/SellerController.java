package com.develhope.spring.controllers.users;

import com.develhope.spring.entities.operations.Order;
import com.develhope.spring.entities.operations.OrderStatus;
import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.services.users.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @GetMapping("/vehicle/get/{id}")
    public Vehicle getDetailsOfVehicle(@PathVariable long id) {
        return sellerService.getDetailsOfVehicle(id);
    }

    @PostMapping("/order/create/{id}")
    public Order createOrderFromVehicle(@PathVariable long id, @RequestBody Order order) {
        return sellerService.createOrderFromVehicle(order, id);
    }

    @DeleteMapping("/order/delete/{id}")
    public Boolean deleteOrder(@PathVariable long id) {
        return sellerService.deleteOrder(id);
    }

    @PutMapping("/order/update/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return sellerService.updateOrder(id, order);
    }

    @GetMapping("/order/status/get/{id}")
    public OrderStatus getOrderStatusFromId(@PathVariable Long id) {
        return sellerService.getOrderStatusFromId(id);
    }

    @PatchMapping("/order/status/update/{id}")
    public Order updateOrderStatusFromId(@PathVariable Long id, @RequestParam String status) {
        return sellerService.updateOrderStatusFromId(id, status);
    }

    @GetMapping("/order/get/by/status")
    public List<Order> getOrdersByStatus(String status) {
        return sellerService.getOrdersByStatus(status);
    }
}
