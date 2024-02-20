package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.Order;
import com.develhope.spring.features.orders.OrderStatus;
import com.develhope.spring.features.vehicle.Vehicle;
import com.develhope.spring.features.orders.OrderRepository;
import com.develhope.spring.features.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    public Order createOrderFromVehicle(Order order, long id) {
        Vehicle vehicle = vehicleRepository.findById(id).get();
        order.setVehicle(vehicle);
        return orderRepository.saveAndFlush(order);
    }

    public Boolean deleteOrder(long id) {
        orderRepository.deleteById(id);
        return !orderRepository.existsById(id);
    }

    public Order updateOrder(long id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            return orderRepository.saveAndFlush(order);
        } else {
            return null;
        }
    }

    public OrderStatus getOrderStatusFromId(long id) {
        if (orderRepository.existsById(id)) {
            return orderRepository.findById(id).get().getOrderStatus();
        } else {
            return null;
        }
    }

    public Order updateOrderStatusFromId(long id, String status) {
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

    public List<Order> findByStatus(String status) {
        String statusString = status.toUpperCase();
        OrderStatus s = OrderStatus.valueOf(statusString);
        return new ArrayList<>(orderRepository.findByOrderStatus(s));
    }
}
