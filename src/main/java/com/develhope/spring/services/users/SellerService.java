package com.develhope.spring.services.users;

import com.develhope.spring.entities.operations.Order;
import com.develhope.spring.entities.operations.OrderStatus;
import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.repositories.OrderRepository;
import com.develhope.spring.repositories.UserRepository;
import com.develhope.spring.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class SellerService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    OrderRepository orderRepository;

    public Vehicle getDetailsOfVehicle(long id) {
        if (orderRepository.existsById(id)) {
            return vehicleRepository.findById(id).get();
        } else {
            return null;
        }
    }

    public Order createOrderFromVehicle(Order order, Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).get();
        order.setVehicle(vehicle);
        return orderRepository.saveAndFlush(order);
    }

    public Boolean deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return !orderRepository.existsById(id);
    }

    public Order updateOrder(Long id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            return orderRepository.saveAndFlush(order);
        } else {
            return null;
        }
    }

    public OrderStatus getOrderStatusFromId(Long id) {
        if (orderRepository.existsById(id)) {
            return orderRepository.findById(id).get().getOrderStatus();
        } else {
            return null;
        }
    }

    public Order updateOrderStatusFromId(Long id, String status) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            String statusString = status.toUpperCase();
            OrderStatus s = OrderStatus.valueOf(statusString);
            order.get().setOrderStatus(s);
            return orderRepository.saveAndFlush(order.get());
        } else {
            return null;
        }
    }

    public List<Order> getOrdersByStatus(String status) {
        String statusString = status.toUpperCase();
        OrderStatus s = OrderStatus.valueOf(statusString);
        return new ArrayList<>(orderRepository.findByOrderStatus(s));
    }
}
