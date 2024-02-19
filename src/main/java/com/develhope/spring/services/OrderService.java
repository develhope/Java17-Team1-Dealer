package com.develhope.spring.services;

import com.develhope.spring.entities.operations.Order;
import com.develhope.spring.entities.operations.OrderStatus;
import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.repositories.OrderRepository;
import com.develhope.spring.repositories.VehicleRepository;
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
        Optional<Order> foundOrder = orderRepository.findById(id);
        if (foundOrder.isPresent()) {
            foundOrder.get().setDeposit(order.getDeposit());
            foundOrder.get().setPaymentStatus(order.getPaymentStatus());
            foundOrder.get().setOrderStatus(order.getOrderStatus());
            foundOrder.get().setUser(order.getUser());
            foundOrder.get().setVehicle(order.getVehicle());
            return orderRepository.saveAndFlush(foundOrder.get());
        } else {
            return null;
        }
    }

    public OrderStatus getOrderStatusFromId(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get().getOrderStatus();
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
